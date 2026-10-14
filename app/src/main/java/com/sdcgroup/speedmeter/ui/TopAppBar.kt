package com.sdcgroup.speedmeter.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.data.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMeterTopAppBar(
    title: String,
    themeMode: ThemeMode,
    onQuitClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier
) {
    val quitDescription = stringResource(id = R.string.quit_button_description)
    key(themeMode) {
        TopAppBar(
            modifier = modifier,
            title = { Text(title) },
            actions = {
                IconButton(onClick = onQuitClick) {
                    Icon(
                        imageVector = Icons.Rounded.PowerSettingsNew,
                        contentDescription = quitDescription
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}
