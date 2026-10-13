package com.sdcgroup.speedmeter.service

import android.app.AppOpsManager
import android.app.Service
import android.app.usage.NetworkStatsManager
import android.content.Intent
import android.net.TrafficStats
import android.os.IBinder
import android.os.Process
import com.sdcgroup.speedmeter.data.DataUnit
import com.sdcgroup.speedmeter.data.Speed
import com.sdcgroup.speedmeter.data.SpeedUnit
import com.sdcgroup.speedmeter.data.dataUnitFlow
import com.sdcgroup.speedmeter.data.formatDataUsage
import com.sdcgroup.speedmeter.data.formatSpeed
import com.sdcgroup.speedmeter.data.getDailyUsageBaseline
import com.sdcgroup.speedmeter.data.saveDailyUsageBaseline
import com.sdcgroup.speedmeter.data.speedUnitFlow
import com.sdcgroup.speedmeter.notification.IconGenerator
import com.sdcgroup.speedmeter.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.NetworkInterface
import java.time.LocalDate
import java.util.Calendar

class SpeedMonitorService : Service() {

    companion object {
        private const val NETWORK_TYPE_MOBILE = 0
        private const val NETWORK_TYPE_WIFI = 1
    }

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var notificationHelper: NotificationHelper
    private val iconGenerator = IconGenerator()

    private var lastRxBytes = 0L
    private var lastTxBytes = 0L
    private var lastTimestamp = 0L
    private var isMonitoring = false

    private var currentSpeedUnit = SpeedUnit.BYTES_PER_SECOND
    private var currentDataUnit = DataUnit.BYTES_BINARY

    private var dailyDate = ""
    private var dailyDataBaseline = 0L
    private var dailyWifiBaseline = 0L

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()

        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        lastTimestamp = System.currentTimeMillis()

        scope.launch { speedUnitFlow(this@SpeedMonitorService).collect { currentSpeedUnit = it } }
        scope.launch { dataUnitFlow(this@SpeedMonitorService).collect { currentDataUnit = it } }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initialSpeed = Speed(
            down = formatSpeed(0L, currentSpeedUnit),
            up = formatSpeed(0L, currentSpeedUnit),
            total = formatSpeed(0L, currentSpeedUnit)
        )
        val initialIcon = iconGenerator.generateIcon(initialSpeed.total)
        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            notificationHelper.buildNotification(
                initialSpeed,
                initialIcon,
                formatDataUsage(0L, currentDataUnit),
                formatDataUsage(0L, currentDataUnit)
            )
        )

        if (!isMonitoring) {
            isMonitoring = true
            scope.launch {
                ensureDailyBaseline()
                while (isActive) {
                    ensureDailyBaseline()
                    val speed = measureNetworkSpeed()
                    val icon = iconGenerator.generateIcon(speed.total)
                    val (dataUsage, wifiUsage) = getTodayUsage()
                    val totalDataUsage = formatDataUsage(dataUsage, currentDataUnit)
                    val wifiDataUsage = formatDataUsage(wifiUsage, currentDataUnit)
                    notificationHelper.notify(speed, icon, totalDataUsage, wifiDataUsage)
                    delay(1000)
                }
            }
        }
        return START_STICKY
    }

    private fun getTodayUsage(): Pair<Long, Long> {
        return if (isUsageAccessGranted()) {
            val mobileToday = queryBytesForTypeToday(NETWORK_TYPE_MOBILE)
            val wifiToday = queryBytesForTypeToday(NETWORK_TYPE_WIFI)
            mobileToday to wifiToday
        } else {
            val dataUsage = (currentDataBytes() - dailyDataBaseline).coerceAtLeast(0L)
            val wifiUsage = (currentWifiBytes() - dailyWifiBaseline).coerceAtLeast(0L)
            dataUsage to wifiUsage
        }
    }

    private fun isUsageAccessGranted(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun startOfTodayMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun queryBytesForTypeToday(networkType: Int): Long {
        return try {
            val statsManager =
                getSystemService(android.content.Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
            val start = startOfTodayMillis()
            val end = System.currentTimeMillis()
            val bucket = statsManager.querySummaryForDevice(networkType, null, start, end)
            (bucket.rxBytes + bucket.txBytes).coerceAtLeast(0L)
        } catch (e: Exception) {
            0L
        }
    }

    private suspend fun ensureDailyBaseline() {
        val today = LocalDate.now().toString()
        if (dailyDate.isEmpty()) {
            val stored = getDailyUsageBaseline(this)
            if (stored.date == today) {
                dailyDate = stored.date
                dailyDataBaseline = stored.dataBaseline
                dailyWifiBaseline = stored.wifiBaseline
                return
            }
        }
        if (dailyDate != today) {
            dailyDate = today
            dailyDataBaseline = currentDataBytes()
            dailyWifiBaseline = currentWifiBytes()
            saveDailyUsageBaseline(this, dailyDate, dailyDataBaseline, dailyWifiBaseline)
        }
    }

    private fun currentTotalBytes(): Long {
        val rx = TrafficStats.getTotalRxBytes()
        val tx = TrafficStats.getTotalTxBytes()
        val safeRx = if (rx == TrafficStats.UNSUPPORTED.toLong()) 0L else rx
        val safeTx = if (tx == TrafficStats.UNSUPPORTED.toLong()) 0L else tx
        return safeRx + safeTx
    }

    private fun currentWifiBytes(): Long {
        return try {
            val wifiInterfaceName = NetworkInterface.getNetworkInterfaces()
                ?.toList()
                ?.firstOrNull { it.name.startsWith("wlan") }
                ?.name

            if (wifiInterfaceName != null) {
                val rx = TrafficStats.getRxBytes(wifiInterfaceName)
                val tx = TrafficStats.getTxBytes(wifiInterfaceName)
                val safeRx = if (rx < 0) 0L else rx
                val safeTx = if (tx < 0) 0L else tx
                safeRx + safeTx
            } else {
                0L
            }
        } catch (e: Exception) {
            0L
        }
    }

    private fun currentDataBytes(): Long {
        return (currentTotalBytes() - currentWifiBytes()).coerceAtLeast(0L)
    }

    private fun measureNetworkSpeed(): Speed {
        val currentRxBytes = TrafficStats.getTotalRxBytes()
        val currentTxBytes = TrafficStats.getTotalTxBytes()
        val currentTimestamp = System.currentTimeMillis()

        val elapsedSeconds =
            ((currentTimestamp - lastTimestamp).coerceAtLeast(1)) / 1000.0

        val rxDelta = if (currentRxBytes >= lastRxBytes && currentRxBytes != TrafficStats.UNSUPPORTED.toLong()) {
            currentRxBytes - lastRxBytes
        } else 0L

        val txDelta = if (currentTxBytes >= lastTxBytes && currentTxBytes != TrafficStats.UNSUPPORTED.toLong()) {
            currentTxBytes - lastTxBytes
        } else 0L

        val downBytesPerSecond = (rxDelta / elapsedSeconds).toLong()
        val upBytesPerSecond = (txDelta / elapsedSeconds).toLong()

        lastRxBytes = currentRxBytes
        lastTxBytes = currentTxBytes
        lastTimestamp = currentTimestamp

        return Speed(
            down = formatSpeed(downBytesPerSecond, currentSpeedUnit),
            up = formatSpeed(upBytesPerSecond, currentSpeedUnit),
            total = formatSpeed(downBytesPerSecond + upBytesPerSecond, currentSpeedUnit)
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isMonitoring = false
        scope.cancel()
    }
}
