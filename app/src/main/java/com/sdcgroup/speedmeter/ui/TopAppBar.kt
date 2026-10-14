package com.sdcgroup.speedmeter.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.data.ThemeMode

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SpeedMeterTopAppBar(
    title: String,
    themeMode: ThemeMode,
    onQuitClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier
) {
    val quitLabel = stringResource(id = R.string.quit_button_label)
    key(themeMode) {
        TopAppBar(
            modifier = modifier,
            title = { Text(title) },
            actions = {
                Button(
                    onClick = onQuitClick,
                    shapes = ButtonDefaults.shapes(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    contentPadding = ButtonDefaults.contentPaddingFor(
                        ButtonDefaults.MinHeight
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PowerOff,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MinHeight)),
                    )
                    Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MinHeight)))
                    Text(quitLabel)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}
