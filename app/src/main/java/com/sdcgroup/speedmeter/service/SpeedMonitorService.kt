package com.sdcgroup.speedmeter.service

import android.app.Service
import android.content.Intent
import android.net.TrafficStats
import android.os.Build
import android.os.IBinder
import com.sdcgroup.speedmeter.data.Speed
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

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()

        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        lastTimestamp = System.currentTimeMillis()
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
            notificationHelper.buildNotification(initialSpeed, initialIcon)
        )

        scope.launch {
            while (isActive) {
                val speed = measureNetworkSpeed()
                val icon = iconGenerator.generateIcon(speed.total)
                notificationHelper.notify(speed, icon)
                delay(1000)
            }
        }
        return START_STICKY
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
        scope.cancel()
    }
}
