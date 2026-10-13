package com.sdcgroup.speedmeter.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.sdcgroup.speedmeter.data.indicatorEnabledFlow
import com.sdcgroup.speedmeter.data.setIndicatorEnabled
import com.sdcgroup.speedmeter.service.SpeedMonitorService
import kotlinx.coroutines.launch

private fun startIndicatorService(context: Context) {
    val intent = Intent(context, SpeedMonitorService::class.java)
    ContextCompat.startForegroundService(context, intent)
}

private fun stopIndicatorService(context: Context) {
    val intent = Intent(context, SpeedMonitorService::class.java)
    context.stopService(intent)
}

@Composable
fun IndicatorEnableSetting(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val enabledFlow = remember { indicatorEnabledFlow(context) }
    val enabled by enabledFlow.collectAsState(initial = true)

    ListItem(
        modifier = modifier.fillMaxWidth(),
        headlineContent = { Text("Enable indicator") },
        supportingContent = { Text("Show NetSpeed indicator in the status bar") },
        trailingContent = {
            Switch(
                checked = enabled,
                onCheckedChange = { newValue ->
                    scope.launch {
                        setIndicatorEnabled(context, newValue)
                        if (newValue) {
                            startIndicatorService(context)
                        } else {
                            stopIndicatorService(context)
                        }
                    }
                },
                contentDescription = "Toggle NetSpeed status bar indicator"
            )
        }
    )
}
