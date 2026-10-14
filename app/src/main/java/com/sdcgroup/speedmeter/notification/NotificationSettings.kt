package com.sdcgroup.speedmeter.notification

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DataUsage
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.data.DataUnit
import com.sdcgroup.speedmeter.data.IconSpeedType
import com.sdcgroup.speedmeter.data.SpeedUnit
import com.sdcgroup.speedmeter.data.ThemeMode
import com.sdcgroup.speedmeter.data.iconSpeedTypeFlow
import com.sdcgroup.speedmeter.data.setIconSpeedType
import com.sdcgroup.speedmeter.data.setStartOnBoot
import com.sdcgroup.speedmeter.data.startOnBootFlow
import com.sdcgroup.speedmeter.service.GeneralSection
import com.sdcgroup.speedmeter.service.SupportSection
import com.sdcgroup.speedmeter.ui.AppAlertDialog
import com.sdcgroup.speedmeter.ui.Switch
import kotlinx.coroutines.launch

internal enum class MoreAction {
    NOTIFICATIONS, SPEED_UNIT, DATA_UNIT, ICON_SPEED_TYPE, USAGE_ACCESS, THEME, START_ON_BOOT, BATTERY_OPTIMIZATION, HELP, ABOUT
}

internal data class MoreItemSpec(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val action: MoreAction? = null,
    val trailingSwitch: Boolean? = null,
)

@Composable
fun SpeedMeterHomeMoreContent(
    themeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode, () -> Unit) -> Unit,
    speedUnit: SpeedUnit,
    onSpeedUnitSelected: (SpeedUnit, () -> Unit) -> Unit,
    dataUnit: DataUnit,
    onDataUnitSelected: (DataUnit, () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val iconSpeedTypeFlowState = remember { iconSpeedTypeFlow(context) }
    val iconSpeedType by iconSpeedTypeFlowState.collectAsState(initial = IconSpeedType.TOTAL)

    val startOnBootFlowState = remember { startOnBootFlow(context) }
    val startOnBoot by startOnBootFlowState.collectAsState(initial = false)

    fun onIconSpeedTypeSelected(type: IconSpeedType, onSaved: () -> Unit) {
        scope.launch {
            setIconSpeedType(context, type)
            onSaved()
        }
    }

    fun onStartOnBootToggled(enabled: Boolean) {
        scope.launch { setStartOnBoot(context, enabled) }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeScreen()
            MoreScreen(
                themeMode = themeMode,
                onThemeModeSelected = onThemeModeSelected,
                speedUnit = speedUnit,
                onSpeedUnitSelected = onSpeedUnitSelected,
                dataUnit = dataUnit,
                onDataUnitSelected = onDataUnitSelected,
                iconSpeedType = iconSpeedType,
                onIconSpeedTypeSelected = ::onIconSpeedTypeSelected,
                startOnBoot = startOnBoot,
                onStartOnBootToggled = ::onStartOnBootToggled,
            )
        }
    }
}

@Composable
fun MoreScreen(
    themeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode, () -> Unit) -> Unit,
    speedUnit: SpeedUnit,
    onSpeedUnitSelected: (SpeedUnit, () -> Unit) -> Unit,
    dataUnit: DataUnit,
    onDataUnitSelected: (DataUnit, () -> Unit) -> Unit,
    iconSpeedType: IconSpeedType,
    onIconSpeedTypeSelected: (IconSpeedType, () -> Unit) -> Unit,
    startOnBoot: Boolean,
    onStartOnBootToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showSpeedUnitDialog by rememberSaveable { mutableStateOf(false) }
    var showDataUnitDialog by rememberSaveable { mutableStateOf(false) }
    var showIconSpeedTypeDialog by rememberSaveable { mutableStateOf(false) }

    val notificationTitle = stringResource(id = R.string.more_notification_title)
    val notificationSubtitle = stringResource(id = R.string.more_notification_subtitle)
    val speedUnitTitle = stringResource(id = R.string.more_speed_unit_title)
    val speedUnitSubtitleBytes = stringResource(id = R.string.more_speed_unit_subtitle_bytes)
    val speedUnitSubtitleBits = stringResource(id = R.string.more_speed_unit_subtitle_bits)
    val dataUnitTitle = stringResource(id = R.string.more_data_unit_title)
    val dataUnitSubtitleBitsDecimal = stringResource(id = R.string.more_data_unit_subtitle_bits_decimal)
    val dataUnitSubtitleBytesDecimal = stringResource(id = R.string.more_data_unit_subtitle_bytes_decimal)
    val dataUnitSubtitleBytesBinary = stringResource(id = R.string.more_data_unit_subtitle_bytes_binary)
    val iconSpeedTypeTitle = stringResource(id = R.string.more_icon_speed_type_title)
    val iconSpeedTypeSubtitleTotal = stringResource(id = R.string.more_icon_speed_type_subtitle_total)
    val iconSpeedTypeSubtitleDownload = stringResource(id = R.string.more_icon_speed_type_subtitle_download)
    val iconSpeedTypeSubtitleUpload = stringResource(id = R.string.more_icon_speed_type_subtitle_upload)

    val speedUnitSubtitle = when (speedUnit) {
        SpeedUnit.BYTES_PER_SECOND -> speedUnitSubtitleBytes
        SpeedUnit.BITS_PER_SECOND -> speedUnitSubtitleBits
    }

    val dataUnitSubtitle = when (dataUnit) {
        DataUnit.BITS_DECIMAL -> dataUnitSubtitleBitsDecimal
        DataUnit.BYTES_DECIMAL -> dataUnitSubtitleBytesDecimal
        DataUnit.BYTES_BINARY -> dataUnitSubtitleBytesBinary
    }

    val iconSpeedTypeSubtitle = when (iconSpeedType) {
        IconSpeedType.TOTAL -> iconSpeedTypeSubtitleTotal
        IconSpeedType.DOWNLOAD -> iconSpeedTypeSubtitleDownload
        IconSpeedType.UPLOAD -> iconSpeedTypeSubtitleUpload
    }

    val notificationSettingsItems = remember(speedUnitSubtitle, dataUnitSubtitle, iconSpeedTypeSubtitle) {
        listOf(
            MoreItemSpec(
                icon = Icons.Rounded.Notifications,
                title = notificationTitle,
                subtitle = notificationSubtitle,
                action = MoreAction.NOTIFICATIONS,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.Speed,
                title = speedUnitTitle,
                subtitle = speedUnitSubtitle,
                action = MoreAction.SPEED_UNIT,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.DataUsage,
                title = dataUnitTitle,
                subtitle = dataUnitSubtitle,
                action = MoreAction.DATA_UNIT,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.GraphicEq,
                title = iconSpeedTypeTitle,
                subtitle = iconSpeedTypeSubtitle,
                action = MoreAction.ICON_SPEED_TYPE,
            ),
        )
    }

    fun onItemAction(action: MoreAction?) {
        when (action) {
            MoreAction.NOTIFICATIONS -> {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                try {
                    context.startActivity(intent)
                } catch (_: Exception) { }
            }
            MoreAction.SPEED_UNIT -> showSpeedUnitDialog = true
            MoreAction.DATA_UNIT -> showDataUnitDialog = true
            MoreAction.ICON_SPEED_TYPE -> showIconSpeedTypeDialog = true
            else -> { }
        }
    }

    if (showSpeedUnitDialog) {
        SpeedUnitDialog(
            currentUnit = speedUnit,
            onUnitSelected = { unit ->
                onSpeedUnitSelected(unit) { showSpeedUnitDialog = false }
            },
            onDismiss = { showSpeedUnitDialog = false }
        )
    }

    if (showDataUnitDialog) {
        DataUnitDialog(
            currentUnit = dataUnit,
            onUnitSelected = { unit ->
                onDataUnitSelected(unit) { showDataUnitDialog = false }
            },
            onDismiss = { showDataUnitDialog = false }
        )
    }

    if (showIconSpeedTypeDialog) {
        IconSpeedTypeDialog(
            currentType = iconSpeedType,
            onTypeSelected = { type ->
                onIconSpeedTypeSelected(type) { showIconSpeedTypeDialog = false }
            },
            onDismiss = { showIconSpeedTypeDialog = false }
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MoreSectionBlock(
            title = stringResource(id = R.string.more_section_notification),
            items = notificationSettingsItems,
            onItemAction = ::onItemAction,
        )

        NotificationLayoutSection()

        GeneralSection(
            themeMode = themeMode,
            onThemeModeSelected = onThemeModeSelected,
            startOnBoot = startOnBoot,
            onStartOnBootToggled = onStartOnBootToggled,
        )

        SupportSection()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.more_made_with_love),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun MoreSectionBlock(
    title: String,
    items: List<MoreItemSpec>,
    onItemAction: (MoreAction?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 2.dp),
        )
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    MoreListItem(
                        item = item,
                        onClick = { onItemAction(item.action) },
                    )
                    if (index < items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MoreListItem(
    item: MoreItemSpec,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cd = if (item.subtitle != null) "${item.title}, ${item.subtitle}" else item.title

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                role = Role.Button,
                onClickLabel = item.title,
                onClick = onClick,
            )
            .semantics(mergeDescendants = true) { contentDescription = cd }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (item.subtitle != null) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (item.trailingSwitch != null) {
            Switch(
                checked = item.trailingSwitch,
                onCheckedChange = { onClick() },
                contentDescription = item.title,
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun SpeedUnitDialog(
    currentUnit: SpeedUnit,
    onUnitSelected: (SpeedUnit) -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogLabel = stringResource(id = R.string.speed_unit_dialog_label)
    val closeLabel = stringResource(id = R.string.dialog_close)
    val options = listOf(
        SpeedUnit.BYTES_PER_SECOND to stringResource(id = R.string.more_speed_unit_subtitle_bytes),
        SpeedUnit.BITS_PER_SECOND to stringResource(id = R.string.more_speed_unit_subtitle_bits),
    )

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(id = R.string.more_speed_unit_title),
        icon = Icons.Rounded.Speed,
        dialogLabel = dialogLabel,
        confirmButtonLabel = closeLabel,
        onConfirm = onDismiss,
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { (unit, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraLarge)
                            .selectable(
                                selected = unit == currentUnit,
                                role = Role.RadioButton,
                                onClick = { onUnitSelected(unit) },
                            )
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = unit == currentUnit,
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

@Composable
private fun IconSpeedTypeDialog(
    currentType: IconSpeedType,
    onTypeSelected: (IconSpeedType) -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogLabel = stringResource(id = R.string.icon_speed_type_dialog_label)
    val closeLabel = stringResource(id = R.string.dialog_close)
    val options = listOf(
        IconSpeedType.TOTAL to stringResource(id = R.string.more_icon_speed_type_subtitle_total),
        IconSpeedType.DOWNLOAD to stringResource(id = R.string.more_icon_speed_type_subtitle_download),
        IconSpeedType.UPLOAD to stringResource(id = R.string.more_icon_speed_type_subtitle_upload),
    )

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(id = R.string.more_icon_speed_type_title),
        icon = Icons.Rounded.GraphicEq,
        dialogLabel = dialogLabel,
        confirmButtonLabel = closeLabel,
        onConfirm = onDismiss,
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { (type, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraLarge)
                            .selectable(
                                selected = type == currentType,
                                role = Role.RadioButton,
                                onClick = { onTypeSelected(type) },
                            )
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = type == currentType,
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

@Composable
private fun DataUnitDialog(
    currentUnit: DataUnit,
    onUnitSelected: (DataUnit) -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogLabel = stringResource(id = R.string.data_unit_dialog_label)
    val closeLabel = stringResource(id = R.string.dialog_close)
    val options = listOf(
        DataUnit.BITS_DECIMAL to stringResource(id = R.string.more_data_unit_subtitle_bits_decimal),
        DataUnit.BYTES_DECIMAL to stringResource(id = R.string.more_data_unit_subtitle_bytes_decimal),
        DataUnit.BYTES_BINARY to stringResource(id = R.string.more_data_unit_subtitle_bytes_binary),
    )

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(id = R.string.more_data_unit_title),
        icon = Icons.Rounded.DataUsage,
        dialogLabel = dialogLabel,
        confirmButtonLabel = closeLabel,
        onConfirm = onDismiss,
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { (unit, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraLarge)
                            .selectable(
                                selected = unit == currentUnit,
                                role = Role.RadioButton,
                                onClick = { onUnitSelected(unit) },
                            )
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = unit == currentUnit,
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
