package com.sdcgroup.speedmeter.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch as Material3Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.sdcgroup.speedmeter.ui.theme.MyComposeApplicationTheme

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
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onCheckedChange(newChecked)
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
