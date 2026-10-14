package com.sdcgroup.speedmeter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.sdcgroup.speedmeter.data.indicatorEnabledFlow
import com.sdcgroup.speedmeter.data.quitRequestedFlow
import com.sdcgroup.speedmeter.data.startOnBootFlow
import com.sdcgroup.speedmeter.service.SpeedMonitorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val quitRequested = quitRequestedFlow(context).first()
                val startOnBoot = startOnBootFlow(context).first()
                val indicatorEnabled = indicatorEnabledFlow(context).first()

                if (!quitRequested && startOnBoot && indicatorEnabled) {
                    val serviceIntent = Intent(context, SpeedMonitorService::class.java)
                    ContextCompat.startForegroundService(context, serviceIntent)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
