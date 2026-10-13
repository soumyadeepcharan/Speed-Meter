package com.sdcgroup.speedmeter.service

import android.app.Service
import android.content.Intent
import android.net.TrafficStats
import android.os.IBinder
import com.sdcgroup.speedmeter.data.Speed
import com.sdcgroup.speedmeter.data.formatDataUsage
import com.sdcgroup.speedmeter.data.formatSpeed
import com.sdcgroup.speedmeter.notification.IconGenerator
import com.sdcgroup.speedmeter.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SpeedMonitorService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var notificationHelper: NotificationHelper
    private val iconGenerator = IconGenerator()

    private var lastRxBytes = 0L
    private var lastTxBytes = 0L
    private var lastTimestamp = 0L
    private var isMonitoring = false

    private var sessionStartTotalBytes = 0L
    private var sessionStartWifiBytes = 0L

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()

        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        lastTimestamp = System.currentTimeMillis()

        sessionStartTotalBytes = currentTotalBytes()
        sessionStartWifiBytes = currentWifiBytes()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initialSpeed = Speed(
            down = formatSpeed(0L),
            up = formatSpeed(0L),
            total = formatSpeed(0L)
        )
        val initialIcon = iconGenerator.generateIcon(initialSpeed.total)
        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            notificationHelper.buildNotification(
                initialSpeed,
                initialIcon,
                formatDataUsage(0L),
                formatDataUsage(0L)
            )
        )

        if (!isMonitoring) {
            isMonitoring = true
            scope.launch {
                while (isActive) {
                    val speed = measureNetworkSpeed()
                    val icon = iconGenerator.generateIcon(speed.total)
                    val totalDataUsage = formatDataUsage(currentTotalBytes() - sessionStartTotalBytes)
                    val wifiDataUsage = formatDataUsage(currentWifiBytes() - sessionStartWifiBytes)
                    notificationHelper.notify(speed, icon, totalDataUsage, wifiDataUsage)
                    delay(1000)
                }
            }
        }
        return START_STICKY
    }

    private fun currentTotalBytes(): Long {
        val rx = TrafficStats.getTotalRxBytes()
        val tx = TrafficStats.getTotalTxBytes()
        val safeRx = if (rx == TrafficStats.UNSUPPORTED.toLong()) 0L else rx
        val safeTx = if (tx == TrafficStats.UNSUPPORTED.toLong()) 0L else tx
        return safeRx + safeTx
    }

    private fun currentWifiBytes(): Long {
        val mobileRx = TrafficStats.getMobileRxBytes()
        val mobileTx = TrafficStats.getMobileTxBytes()
        val totalRx = TrafficStats.getTotalRxBytes()
        val totalTx = TrafficStats.getTotalTxBytes()

        val safeMobileRx = if (mobileRx == TrafficStats.UNSUPPORTED.toLong()) 0L else mobileRx
        val safeMobileTx = if (mobileTx == TrafficStats.UNSUPPORTED.toLong()) 0L else mobileTx
        val safeTotalRx = if (totalRx == TrafficStats.UNSUPPORTED.toLong()) 0L else totalRx
        val safeTotalTx = if (totalTx == TrafficStats.UNSUPPORTED.toLong()) 0L else totalTx

        val wifiRx = (safeTotalRx - safeMobileRx).coerceAtLeast(0L)
        val wifiTx = (safeTotalTx - safeMobileTx).coerceAtLeast(0L)
        return wifiRx + wifiTx
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
            down = formatSpeed(downBytesPerSecond),
            up = formatSpeed(upBytesPerSecond),
            total = formatSpeed(downBytesPerSecond + upBytesPerSecond)
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isMonitoring = false
        scope.cancel()
    }
}
