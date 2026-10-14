package com.sdcgroup.speedmeter.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.sdcgroup.speedmeter.BuildConfig
import com.sdcgroup.speedmeter.R
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

private const val ActionHoverStateLayerOpacity = 0.08f
private const val ActionFocusStateLayerOpacity = 0.10f
private const val ActionPressedStateLayerOpacity = 0.10f

private val SwitchTrackWidth = 52.dp
private val SwitchTrackHeight = 32.dp
private val SwitchThumbSize = 24.dp
private val SwitchThumbPadding = 4.dp

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "Toggle switch"
) {
    val density = LocalDensity.current

    val trackWidthPx = with(density) { SwitchTrackWidth.toPx() }
    val thumbSizePx = with(density) { SwitchThumbSize.toPx() }
    val thumbPaddingPx = with(density) { SwitchThumbPadding.toPx() }
    val maxOffsetPx = trackWidthPx - thumbSizePx - (thumbPaddingPx * 2)

    var isDragging by rememberSaveable { mutableStateOf(false) }
    var dragOffsetPx by remember { mutableFloatStateOf(if (checked) maxOffsetPx else 0f) }

    val animatedOffsetPx by animateFloatAsState(
        targetValue = if (isDragging) dragOffsetPx else (if (checked) maxOffsetPx else 0f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "switchThumbOffset"
    )

    val trackColor = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

    val thumbColor = if (checked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline

    val progress = if (maxOffsetPx > 0f) {
        (animatedOffsetPx / maxOffsetPx).coerceIn(0f, 1f)
    } else {
        if (checked) 1f else 0f
    }

    val uncheckedIconAlpha = (1f - (progress / 0.5f)).coerceIn(0f, 1f)
    val checkedIconAlpha = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .size(width = SwitchTrackWidth, height = SwitchTrackHeight)
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Switch
            }
            .clip(CircleShape)
            .background(trackColor)
            .pointerInput(enabled, maxOffsetPx, checked) {
                if (!enabled) return@pointerInput
                awaitEachGesture {
                    val down = awaitFirstDown()
                    var totalDrag = 0f
                    dragOffsetPx = if (checked) maxOffsetPx else 0f
                    isDragging = true

                    var pointerId = down.id
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId } ?: break

                        if (change.changedToUpIgnoreConsumed()) {
                            isDragging = false
                            val draggedEnough = kotlin.math.abs(totalDrag) > 8f
                            val shouldBeChecked = if (draggedEnough) {
                                dragOffsetPx >= maxOffsetPx / 2f
                            } else {
                                !checked
                            }
                            if (shouldBeChecked != checked) {
                                onCheckedChange(shouldBeChecked)
                            }
                            change.consume()
                            break
                        }

                        val positionChange = change.positionChange()
                        if (positionChange.x != 0f) {
                            totalDrag += positionChange.x
                            dragOffsetPx = (dragOffsetPx + positionChange.x).coerceIn(0f, maxOffsetPx)
                            change.consume()
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .padding(SwitchThumbPadding)
                .offset { IntOffset(animatedOffsetPx.roundToInt(), 0) }
                .size(SwitchThumbSize)
                .clip(CircleShape)
                .background(thumbColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .size(14.dp)
                    .graphicsLayer { alpha = uncheckedIconAlpha },
            )
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(14.dp)
                    .graphicsLayer { alpha = checkedIconAlpha },
            )
        }
    }
}

data class AppAlertDialogOption(
    val label: String,
    val selected: Boolean,
    val onSelect: () -> Unit
)

private val CenteredDialogProperties = DialogProperties(decorFitsSystemWindows = true)

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
fun appButtonShapes(): ButtonShapes {
    val sectionShape = MaterialTheme.shapes.extraLarge
    return ButtonDefaults.shapes(
        shape = sectionShape,
        pressedShape = sectionShape.copy(all = CornerSize(30))
    )
}

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    icon: ImageVector,
    confirmButtonLabel: String,
    onConfirm: () -> Unit,
    dialogLabel: String = title,
    text: @Composable (() -> Unit)? = null,
) {
    val configuration = LocalConfiguration.current
    val maxHeight = (configuration.screenHeightDp * 0.5f).dp

    AlertDialog(
        modifier = Modifier.semantics { contentDescription = dialogLabel },
        onDismissRequest = onDismissRequest,
        shape = MaterialTheme.shapes.extraLarge,
        properties = CenteredDialogProperties,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        title = { Text(text = title) },
        text = text?.let {
            {
                Column(
                    modifier = Modifier
                        .heightIn(max = maxHeight)
                        .verticalScroll(rememberScrollState())
                ) {
                    it()
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, shapes = appButtonShapes()) { Text(confirmButtonLabel) }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun HelpFeedbackBottomSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

    fun dismissSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss()
        }
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val sheetHeight = (configuration.screenHeightDp.dp - statusBarHeight) * 0.4f
    val contentAlpha by animateFloatAsState(
        targetValue = if (sheetState.targetValue == SheetValue.Hidden) 0f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "helpSheetSharedAxisY"
    )
    val density = LocalDensity.current
    val sectionShape = MaterialTheme.shapes.extraLarge

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = sectionShape.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        ),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.semantics { contentDescription = dialogLabel },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .graphicsLayer {
                    alpha = contentAlpha
                    translationY = with(density) { (1f - contentAlpha) * 24.dp.toPx() }
                }
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            HelpActionRow(
                icon = Icons.Rounded.Lightbulb,
                label = stringResource(id = R.string.help_suggest_feature),
                onClick = {
                    sendEmail(suggestFeatureSubject, suggestFeatureBody)
                    dismissSheet()
                },
            )
            HelpActionRow(
                icon = Icons.Rounded.BugReport,
                label = stringResource(id = R.string.help_report_bug),
                onClick = {
                    sendEmail(bugReportSubject, bugReportBody)
                    dismissSheet()
                },
            )
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
        animationSpec = tween(durationMillis = 100),
        label = "helpActionStateLayer_$label",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
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
