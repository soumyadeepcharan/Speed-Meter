package com.sdcgroup.speedmeter.service

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryAlert
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.data.ThemeMode
import com.sdcgroup.speedmeter.notification.MoreAction
import com.sdcgroup.speedmeter.notification.MoreItemSpec
import com.sdcgroup.speedmeter.notification.MoreSectionBlock
import com.sdcgroup.speedmeter.notification.isBatteryOptimizationDisabled
import com.sdcgroup.speedmeter.ui.AppAlertDialog

@Composable
internal fun GeneralSection(
    themeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode, () -> Unit) -> Unit,
    startOnBoot: Boolean,
    onStartOnBootToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var showThemeDialog by rememberSaveable { mutableStateOf(false) }
    var isBatteryOptimizationDisabled by remember { mutableStateOf(isBatteryOptimizationDisabled(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isBatteryOptimizationDisabled = isBatteryOptimizationDisabled(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val themeTitle = stringResource(id = R.string.more_theme_title)
    val themeSubtitleSystem = stringResource(id = R.string.more_theme_subtitle_system)
    val themeSubtitleLight = stringResource(id = R.string.more_theme_subtitle_light)
    val themeSubtitleDark = stringResource(id = R.string.more_theme_subtitle_dark)
    val startOnBootTitle = stringResource(id = R.string.more_start_on_boot_title)
    val startOnBootSubtitleEnabled = stringResource(id = R.string.more_start_on_boot_subtitle_enabled)
    val startOnBootSubtitleDisabled = stringResource(id = R.string.more_start_on_boot_subtitle_disabled)
    val batteryOptimizationTitle = stringResource(id = R.string.more_battery_optimization_title)
    val batteryOptimizationSubtitleEnabled = stringResource(id = R.string.more_battery_optimization_subtitle_enabled)
    val batteryOptimizationSubtitleDisabled = stringResource(id = R.string.more_battery_optimization_subtitle_disabled)

    val themeSubtitle = when (themeMode) {
        ThemeMode.SYSTEM -> themeSubtitleSystem
        ThemeMode.LIGHT -> themeSubtitleLight
        ThemeMode.DARK -> themeSubtitleDark
    }

    val startOnBootSubtitle = if (startOnBoot) startOnBootSubtitleEnabled else startOnBootSubtitleDisabled

    val batteryOptimizationSubtitle = if (!isBatteryOptimizationDisabled) {
        batteryOptimizationSubtitleEnabled
    } else {
        batteryOptimizationSubtitleDisabled
    }

    val generalItems = remember(themeSubtitle, startOnBootSubtitle, startOnBoot, batteryOptimizationSubtitle, isBatteryOptimizationDisabled) {
        listOf(
            MoreItemSpec(
                icon = Icons.Rounded.Palette,
                title = themeTitle,
                subtitle = themeSubtitle,
                action = MoreAction.THEME,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.PowerSettingsNew,
                title = startOnBootTitle,
                subtitle = startOnBootSubtitle,
                action = MoreAction.START_ON_BOOT,
                trailingSwitch = startOnBoot,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.BatteryAlert,
                title = batteryOptimizationTitle,
                subtitle = batteryOptimizationSubtitle,
                action = MoreAction.BATTERY_OPTIMIZATION,
                trailingSwitch = !isBatteryOptimizationDisabled,
            ),
        )
    }

    fun onItemAction(action: MoreAction?) {
        when (action) {
            MoreAction.THEME -> showThemeDialog = true
            MoreAction.START_ON_BOOT -> onStartOnBootToggled(!startOnBoot)
            MoreAction.BATTERY_OPTIMIZATION -> {
                if (!isBatteryOptimizationDisabled) {
                    val intent = Intent(
                        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                        Uri.parse("package:${context.packageName}")
                    )
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) { }
                } else {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) { }
                }
            }
            else -> { }
        }
    }

    if (showThemeDialog) {
        ThemeDialog(
            currentMode = themeMode,
            onModeSelected = { mode ->
                onThemeModeSelected(mode) { showThemeDialog = false }
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    MoreSectionBlock(
        title = stringResource(id = R.string.more_section_general),
        items = generalItems,
        onItemAction = ::onItemAction,
        modifier = modifier,
    )
}

@Composable
private fun ThemeDialog(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogLabel = stringResource(id = R.string.theme_dialog_label)
    val closeLabel = stringResource(id = R.string.dialog_close)
    val options = listOf(
        ThemeMode.SYSTEM to stringResource(id = R.string.more_theme_subtitle_system),
        ThemeMode.LIGHT to stringResource(id = R.string.more_theme_subtitle_light),
        ThemeMode.DARK to stringResource(id = R.string.more_theme_subtitle_dark),
    )

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(id = R.string.more_theme_title),
        icon = Icons.Rounded.Palette,
        dialogLabel = dialogLabel,
        confirmButtonLabel = closeLabel,
        onConfirm = onDismiss,
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { (mode, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraLarge)
                            .selectable(
                                selected = mode == currentMode,
                                role = Role.RadioButton,
                                onClick = { onModeSelected(mode) },
                            )
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = mode == currentMode,
                            onClick = null,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    )
}

