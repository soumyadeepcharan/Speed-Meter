package com.sdcgroup.speedmeter.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt

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
    val hapticFeedback = LocalHapticFeedback.current
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

    val trackColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "switchTrackColor"
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "switchThumbColor"
    )

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
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
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
private fun ScrollableDialogContent(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val maxHeight = (configuration.screenHeightDp * 0.5f).dp

    Column(
        modifier = Modifier
            .heightIn(max = maxHeight)
            .verticalScroll(rememberScrollState())
    ) {
        content()
    }
}

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    icon: ImageVector,
    confirmButtonLabel: String,
    onConfirm: () -> Unit,
    dialogLabel: String = title,
    text: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        modifier = Modifier.semantics { contentDescription = dialogLabel },
        onDismissRequest = onDismissRequest,
        properties = CenteredDialogProperties,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        title = { Text(text = title) },
        text = text?.let { { ScrollableDialogContent(content = it) } },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(confirmButtonLabel) }
        },
    )
}

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    icon: ImageVector,
    dismissButtonLabel: String,
    dialogLabel: String = title,
    text: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        modifier = Modifier.semantics { contentDescription = dialogLabel },
        onDismissRequest = onDismissRequest,
        properties = CenteredDialogProperties,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        title = { Text(text = title) },
        text = text?.let { { ScrollableDialogContent(content = it) } },
        confirmButton = { },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(dismissButtonLabel) }
        },
    )
}
