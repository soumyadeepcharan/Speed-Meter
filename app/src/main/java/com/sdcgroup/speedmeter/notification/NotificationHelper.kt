package com.sdcgroup.speedmeter.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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

    private fun isNightMode(): Boolean {
        val mask = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mask == Configuration.UI_MODE_NIGHT_YES
    }

    fun buildNotification(
        speed: Speed,
        iconBitmap: Bitmap,
        totalDataUsage: String,
        wifiDataUsage: String
    ): android.app.Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(IconCompat.createWithBitmap(iconBitmap))
            .setCustomContentView(updateRemoteViews(speed, totalDataUsage, wifiDataUsage))
            .setCustomBigContentView(updateRemoteViews(speed, totalDataUsage, wifiDataUsage))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setLocalOnly(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val night = isNightMode()
            val color = ContextCompat.getColor(
                context,
                if (night) R.color.notification_background_color else R.color.notification_accent_color
            )
            builder.setColor(color).setColorized(night)
        } else {
            builder.setColorized(false)
        }

        return builder.build()
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
