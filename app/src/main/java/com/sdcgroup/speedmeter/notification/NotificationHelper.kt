package com.sdcgroup.speedmeter.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.data.Speed

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "netspeed_channel"
        const val NOTIFICATION_ID = 1001
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.netspeed_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.netspeed_channel_description)
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildNotification(
        speed: Speed,
        iconBitmap: Bitmap,
        totalDataUsage: String,
        wifiDataUsage: String
    ): android.app.Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(IconCompat.createWithBitmap(iconBitmap))
            .setCustomContentView(updateRemoteViews(speed, totalDataUsage, wifiDataUsage))
            .setCustomBigContentView(updateRemoteViews(speed, totalDataUsage, wifiDataUsage))
            .setOngoing(true)
            .setLocalOnly(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    fun notify(speed: Speed, iconBitmap: Bitmap, totalDataUsage: String, wifiDataUsage: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            buildNotification(speed, iconBitmap, totalDataUsage, wifiDataUsage)
        )
    }

    private fun updateRemoteViews(
        speed: Speed,
        totalDataUsage: String,
        wifiDataUsage: String
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_layout)
        remoteViews.setTextViewText(
            R.id.notificationDownText,
            "${speed.down.speedValue}${speed.down.speedUnit}"
        )
        remoteViews.setTextViewText(
            R.id.notificationUpText,
            "${speed.up.speedValue}${speed.up.speedUnit}"
        )
        remoteViews.setTextViewText(
            R.id.notificationDataText,
            totalDataUsage
        )
        remoteViews.setTextViewText(
            R.id.notificationWifiText,
            wifiDataUsage
        )
        return remoteViews
    }
}
