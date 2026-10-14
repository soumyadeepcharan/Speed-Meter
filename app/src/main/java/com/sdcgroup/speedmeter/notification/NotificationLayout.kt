package com.sdcgroup.speedmeter.notification

import android.content.Intent
import android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sdcgroup.speedmeter.R

@Composable
internal fun NotificationLayoutSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isUsageAccessGranted by remember { mutableStateOf(isUsageAccessGranted(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isUsageAccessGranted = isUsageAccessGranted(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val usageAccessTitle = stringResource(id = R.string.usage_access_title)
    val usageAccessSubtitleGranted = stringResource(id = R.string.usage_access_subtitle_granted)
    val usageAccessSubtitleNotGranted = stringResource(id = R.string.usage_access_subtitle_not_granted)
    val usageAccessSubtitle = if (isUsageAccessGranted) usageAccessSubtitleGranted else usageAccessSubtitleNotGranted

    val notificationLayoutItems = remember(usageAccessSubtitle, isUsageAccessGranted) {
        listOf(
            MoreItemSpec(
                icon = Icons.Rounded.Verified,
                title = usageAccessTitle,
                subtitle = usageAccessSubtitle,
                action = MoreAction.USAGE_ACCESS,
                trailingSwitch = isUsageAccessGranted,
            )
        )
    }

    fun onItemAction(action: MoreAction?) {
        when (action) {
            MoreAction.USAGE_ACCESS -> {
                val intent = Intent(ACTION_USAGE_ACCESS_SETTINGS)
                try {
                    context.startActivity(intent)
                } catch (_: Exception) { }
            }
            else -> { }
        }
    }

    SettingsSectionBlock(
        title = stringResource(id = R.string.more_section_notification_layout),
        items = notificationLayoutItems,
        onItemAction = ::onItemAction,
        modifier = modifier,
    )
}
