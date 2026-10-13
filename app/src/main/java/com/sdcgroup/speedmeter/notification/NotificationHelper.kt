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

    fun buildNotification(speed: Speed, iconBitmap: Bitmap): android.app.Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(IconCompat.createWithBitmap(iconBitmap))
            .setCustomContentView(createRemoteViews(speed))
            .setOngoing(true)
            .setLocalOnly(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .build()
    }

    fun notify(speed: Speed, iconBitmap: Bitmap) {
        notificationManager.notify(NOTIFICATION_ID, buildNotification(speed, iconBitmap))
    }

    private fun createRemoteViews(speed: Speed): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.notification_layout)
        views.setTextViewText(R.id.speedValue, speed.total.speedValue)
        views.setTextViewText(R.id.speedUnit, speed.total.speedUnit)
        views.setTextViewText(
            R.id.speedDetails,
            "${context.getString(R.string.download_label)}: ${speed.down.speedValue} ${speed.down.speedUnit}  " +
                "${context.getString(R.string.upload_label)}: ${speed.up.speedValue} ${speed.up.speedUnit}"
        )
        return views
    }
}
