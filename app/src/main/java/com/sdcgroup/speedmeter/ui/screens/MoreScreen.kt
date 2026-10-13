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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.sdcgroup.speedmeter.BuildConfig

private val DialogContainerShape = RoundedCornerShape(28.dp)
private val DialogElevation = 6.dp
private val DialogDividerHeight = 1.dp

private const val ActionHoverStateLayerOpacity = 0.08f
private const val ActionFocusStateLayerOpacity = 0.10f
private const val ActionPressedStateLayerOpacity = 0.10f

private val DialogHeadlineTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = 0.sp,
)

private val DialogActionTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
)

private enum class MoreAction {
    NOTIFICATIONS, HELP, ABOUT
}

private data class MoreItemSpec(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val action: MoreAction? = null,
)

@Composable
fun MoreScreen(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showHelpDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val generalItems = remember {
        listOf(
            MoreItemSpec(
                icon = Icons.Rounded.Notifications,
                title = "Notification settings",
                subtitle = "Manage NetSpeed indicator notifications",
                action = MoreAction.NOTIFICATIONS,
            )
        )
    }

    val supportItems = remember {
        listOf(
            MoreItemSpec(
                icon = Icons.AutoMirrored.Rounded.Help,
                title = "Help & feedback",
                subtitle = "Suggest a feature or report a bug",
                action = MoreAction.HELP,
            ),
            MoreItemSpec(
                icon = Icons.Rounded.Info,
                title = "About",
                subtitle = "v${BuildConfig.VERSION_NAME}",
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

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(key = "section_general") {
            MoreSectionBlock(
                title = "General",
                items = generalItems,
                onItemAction = ::onItemAction,
            )
        }

        item(key = "section_appearance") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp),
                )
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                imageVector = Icons.Rounded.DarkMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Dark theme",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "Switch between light and dark appearance",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onThemeToggle,
                            contentDescription = "Toggle dark theme",
                        )
                    }
                }
            }
        }

        item(key = "section_support") {
            MoreSectionBlock(
                title = "Support",
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
                    text = "Speed Meter",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpFeedbackDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val dialogLabel = "Help and feedback"

    val appVersion = BuildConfig.VERSION_NAME
    val modelName = Build.MODEL
    val modelNumber = Build.DEVICE
    val androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val bugReportSubject = "[Enter A Brief Subject For The Issue]"
    val bugReportBody = """
Dear Developer,

I would like to report an issue encountered in Speed Meter.
Bug Summary: [Provide a short, clear description of the issue.]
App Version: $appVersion
Model Name: $modelName
Model Number: $modelNumber
Android Version: $androidVersion
Please Explain Necessary Steps to Reproduce the Issue:
1. Open the app
2. Navigate to [specific screen]
3. Tap on [button/feature]
4. Observe the issue
Expected Behavior: [Describe what should have happened.]
Actual Behavior: [Describe what actually happened.]
Frequency: [Always / Sometimes / Rarely]
Attachments (if any): [Screenshots / Screen recording / Logs]

Please let me know if any additional information is required. I would appreciate your support in resolving this issue.
Thank you for your time and assistance.

Best regards,
[Your Name]
    """.trimIndent()

    val suggestFeatureSubject = "[Enter A Brief Subject For The Feature]"
    val suggestFeatureBody = """
Dear Developer,

I would like to suggest a feature which would be useful for Speed Meter.
Feature Summary: [Provide a short, clear description of the feature and how it would be useful.]

Please let me know if any additional information is required.
Thank you for your time and assistance.

Best regards,
[Your Name]
    """.trimIndent()

    fun sendEmail(subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("sdcgroupofficial@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) { }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(min = 280.dp, max = 560.dp)
                .fillMaxWidth()
                .semantics { contentDescription = dialogLabel },
            shape = DialogContainerShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = DialogElevation,
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "How can we help?",
                    style = DialogHeadlineTextStyle,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = DialogDividerHeight,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    HelpActionRow(
                        icon = Icons.Rounded.Lightbulb,
                        label = "Suggest a feature",
                        onClick = {
                            sendEmail(suggestFeatureSubject, suggestFeatureBody)
                            onDismiss()
                        },
                        modifier = Modifier.focusRequester(focusRequester),
                    )
                    HelpActionRow(
                        icon = Icons.Rounded.BugReport,
                        label = "Report a bug",
                        onClick = {
                            sendEmail(bugReportSubject, bugReportBody)
                            onDismiss()
                        },
                    )
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = DialogDividerHeight,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    DialogTextButton(
                        label = "Cancel",
                        onClick = onDismiss,
                    )
                }
            }
        }
    }
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
            style = DialogActionTextStyle,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val dialogLabel = "About Speed Meter"

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(min = 280.dp, max = 560.dp)
                .fillMaxWidth()
                .semantics { contentDescription = dialogLabel },
            shape = DialogContainerShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = DialogElevation,
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Speed Meter",
                    style = DialogHeadlineTextStyle,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Version ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = DialogDividerHeight,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "A lightweight data transfer speed monitor for Android.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    DialogTextButton(
                        label = "Close",
                        onClick = onDismiss,
                        modifier = Modifier.focusRequester(focusRequester),
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogTextButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val primaryColor = MaterialTheme.colorScheme.primary

    val stateLayerOpacity = when {
        isPressed -> ActionPressedStateLayerOpacity
        isFocused -> ActionFocusStateLayerOpacity
        isHovered -> ActionHoverStateLayerOpacity
        else -> 0f
    }

    val animatedStateLayerColor by animateColorAsState(
        targetValue = primaryColor.copy(alpha = stateLayerOpacity),
        label = "dialogActionStateLayer",
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(animatedStateLayerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = primaryColor),
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = DialogActionTextStyle,
            color = primaryColor,
        )
    }
}
