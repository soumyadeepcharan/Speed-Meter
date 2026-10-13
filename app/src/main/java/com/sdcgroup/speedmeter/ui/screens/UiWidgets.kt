package com.sdcgroup.speedmeter.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch as Material3Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "Toggle switch"
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    Material3Switch(
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        checked = checked,
        onCheckedChange = { newChecked ->
            onCheckedChange(newChecked)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        enabled = enabled,
        interactionSource = interactionSource,
        thumbContent = {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        }
    )
}

data class AppAlertDialogOption(
    val label: String,
    val selected: Boolean,
    val onSelect: () -> Unit
)

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
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        title = { Text(text = title) },
        text = text,
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
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        title = { Text(text = title) },
        text = text,
        confirmButton = { },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(dismissButtonLabel) }
        },
    )
}
