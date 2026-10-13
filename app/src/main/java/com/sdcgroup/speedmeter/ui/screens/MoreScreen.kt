package com.sdcgroup.speedmeter.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
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
import com.sdcgroup.speedmeter.BuildConfig
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.data.ThemeMode
import com.sdcgroup.speedmeter.data.setThemeMode
import com.sdcgroup.speedmeter.data.themeModeFlow
import kotlinx.coroutines.launch

private const val ActionHoverStateLayerOpacity = 0.08f
private const val ActionFocusStateLayerOpacity = 0.10f
private const val ActionPressedStateLayerOpacity = 0.10f

private enum class MoreAction {
    NOTIFICATIONS, THEME, HELP, ABOUT
}

private data class MoreItemSpec(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val action: MoreAction? = null,
)

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showHelpDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    val themeModeFlow = remember { themeModeFlow(context) }
    val themeMode by themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM)

    val notificationTitle = stringResource(id = R.string.more_notification_title)
    val notificationSubtitle = stringResource(id = R.string.more_notification_subtitle)
    val themeTitle = stringResource(id = R.string.more_theme_title)
    val themeSubtitleSystem = stringResource(id = R.string.more_theme_subtitle_system)
    val themeSubtitleLight = stringResource(id = R.string.more_theme_subtitle_light)
    val themeSubtitleDark = stringResource(id = R.string.more_theme_subtitle_dark)
    val helpTitle = stringResource(id = R.string.more_help_title)
    val helpSubtitle = stringResource(id = R.string.more_help_subtitle)
    val aboutTitle = stringResource(id = R.string.more_about_title)
    val aboutSubtitle = stringResource(id = R.string.more_about_subtitle, BuildConfig.VERSION_NAME)

    val themeSubtitle = when (themeMode) {
        ThemeMode.SYSTEM -> themeSubtitleSystem
        ThemeMode.LIGHT -> themeSubtitleLight
        ThemeMode.DARK -> themeSubtitleDark
    }

    val generalItems = remember {
        listOf(
            MoreItemSpec(
                icon = Icons.Rounded.Notifications,
                title = notificationTitle,
                subtitle = notificationSubtitle,
                action = MoreAction.NOTIFICATIONS,
            )
        )
    }

    val appearanceItems = remember(themeSubtitle) {
        listOf(
            MoreItemSpec(
                icon = Icons.Rounded.Palette,
                title = themeTitle,
                subtitle = themeSubtitle,
                action = MoreAction.THEME,
            )
        )
    }

    val supportItems = remember(helpSubtitle, aboutSubtitle) {
        listOf(
            MoreItemSpec(
                icon = Icons.AutoMirrored.Rounded.Help,
                title = helpTitle,
                subtitle = helpSubtitle,
                action = MoreAction.HELP,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.Info,
                title = aboutTitle,
                subtitle = aboutSubtitle,
                action = MoreAction.ABOUT,
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
            MoreAction.THEME -> showThemeDialog = true
            MoreAction.HELP -> showHelpDialog = true
            MoreAction.ABOUT -> showAboutDialog = true
            else -> { }
        }
    }

    if (showHelpDialog) {
        HelpFeedbackDialog(onDismiss = { showHelpDialog = false })
    }

    if (showAboutDialog) {
        AboutDialog(onDismiss = { showAboutDialog = false })
    }

    if (showThemeDialog) {
        ThemeDialog(
            currentMode = themeMode,
            onModeSelected = { mode ->
                scope.launch { setThemeMode(context, mode) }
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(key = "section_general") {
            MoreSectionBlock(
                title = stringResource(id = R.string.more_section_general),
                items = generalItems,
                onItemAction = ::onItemAction,
            )
        }

        item(key = "section_appearance") {
            MoreSectionBlock(
                title = stringResource(id = R.string.more_section_appearance),
                items = appearanceItems,
                onItemAction = ::onItemAction,
            )
        }

        item(key = "section_support") {
            MoreSectionBlock(
                title = stringResource(id = R.string.more_section_support),
                items = supportItems,
                onItemAction = ::onItemAction,
            )
        }

        item(key = "footer") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
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
}

@Composable
private fun MoreSectionBlock(
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
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun HelpFeedbackDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    val appVersion = BuildConfig.VERSION_NAME
    val modelName = Build.MODEL
    val modelNumber = Build.DEVICE
    val androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val bugReportSubject = stringResource(id = R.string.email_bug_report_subject)
    val bugReportBody = stringResource(
        id = R.string.email_bug_report_body,
        appVersion, modelName, modelNumber, androidVersion
    )
    val suggestFeatureSubject = stringResource(id = R.string.email_feature_subject)
    val suggestFeatureBody = stringResource(id = R.string.email_feature_body)
    val emailRecipient = stringResource(id = R.string.email_recipient)
    val dialogLabel = stringResource(id = R.string.help_dialog_label)
    val dialogTitle = stringResource(id = R.string.help_dialog_title)
    val cancelLabel = stringResource(id = R.string.dialog_cancel)

    fun sendEmail(subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailRecipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) { }
    }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = dialogTitle,
        icon = Icons.AutoMirrored.Rounded.Help,
        dialogLabel = dialogLabel,
        dismissButtonLabel = cancelLabel,
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                HelpActionRow(
                    icon = Icons.Rounded.Lightbulb,
                    label = stringResource(id = R.string.help_suggest_feature),
                    onClick = {
                        sendEmail(suggestFeatureSubject, suggestFeatureBody)
                        onDismiss()
                    },
                )
                HelpActionRow(
                    icon = Icons.Rounded.BugReport,
                    label = stringResource(id = R.string.help_report_bug),
                    onClick = {
                        sendEmail(bugReportSubject, bugReportBody)
                        onDismiss()
                    },
                )
            }
        }
    )
}

@Composable
private fun HelpActionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val onContainerColor = MaterialTheme.colorScheme.onSecondaryContainer

    val stateLayerOpacity = when {
        isPressed -> ActionPressedStateLayerOpacity
        isFocused -> ActionFocusStateLayerOpacity
        isHovered -> ActionHoverStateLayerOpacity
        else -> 0f
    }

    val animatedStateLayerColor by animateColorAsState(
        targetValue = onContainerColor.copy(alpha = stateLayerOpacity),
        label = "helpActionStateLayer_$label",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(containerColor)
            .background(animatedStateLayerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = onContainerColor),
                role = Role.Button,
                onClickLabel = label,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = onContainerColor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = onContainerColor,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = onContainerColor.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    val dialogLabel = stringResource(id = R.string.about_dialog_label)
    val closeLabel = stringResource(id = R.string.dialog_close)
    val versionText = stringResource(id = R.string.about_version, BuildConfig.VERSION_NAME)
    val descriptionText = stringResource(id = R.string.about_description)

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(id = R.string.app_name),
        icon = Icons.Rounded.Info,
        dialogLabel = dialogLabel,
        confirmButtonLabel = closeLabel,
        onConfirm = onDismiss,
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = versionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
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
                            .selectable(
                                selected = mode == currentMode,
                                role = Role.RadioButton,
                                onClick = { onModeSelected(mode) },
                            )
                            .padding(vertical = 8.dp),
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
