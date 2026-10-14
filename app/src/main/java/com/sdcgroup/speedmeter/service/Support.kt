package com.sdcgroup.speedmeter.service

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sdcgroup.speedmeter.BuildConfig
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.notification.MoreAction
import com.sdcgroup.speedmeter.notification.MoreItemSpec
import com.sdcgroup.speedmeter.notification.MoreSectionBlock
import com.sdcgroup.speedmeter.ui.AppAlertDialog
import com.sdcgroup.speedmeter.ui.HelpFeedbackBottomSheet

@Composable
internal fun SupportSection(modifier: Modifier = Modifier) {
    var showHelpSheet by rememberSaveable { mutableStateOf(false) }
    var showAboutDialog by rememberSaveable { mutableStateOf(false) }

    val helpTitle = stringResource(id = R.string.more_help_title)
    val helpSubtitle = stringResource(id = R.string.more_help_subtitle)
    val aboutTitle = stringResource(id = R.string.more_about_title)
    val aboutSubtitle = stringResource(id = R.string.more_about_subtitle, BuildConfig.VERSION_NAME)

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
            MoreAction.HELP -> showHelpSheet = true
            MoreAction.ABOUT -> showAboutDialog = true
            else -> { }
        }
    }

    if (showHelpSheet) {
        HelpFeedbackBottomSheet(onDismiss = { showHelpSheet = false })
    }

    if (showAboutDialog) {
        AboutDialog(onDismiss = { showAboutDialog = false })
    }

    MoreSectionBlock(
        title = stringResource(id = R.string.more_section_support),
        items = supportItems,
        onItemAction = ::onItemAction,
        modifier = modifier,
    )
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
