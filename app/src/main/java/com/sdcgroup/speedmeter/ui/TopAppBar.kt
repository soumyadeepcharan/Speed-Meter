package com.sdcgroup.speedmeter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.layout
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            title = { Text(title, modifier = Modifier.padding(start = 16.dp)) },
            modifier = modifier,
            actions = {
                Box(
                    modifier = Modifier.layout { measurable, _ ->
                        val placeable = measurable.measure(androidx.compose.ui.unit.Constraints())
                        val reserved = maxOf(108.dp.roundToPx(), placeable.width)
                        layout(reserved, placeable.height) {
                            placeable.place((reserved - placeable.width) / 2, 0)
                        }
                    }
                ) {
                    Button(
                        onClick = onQuitClick,
                        modifier = Modifier.size(width = SwitchTrackWidth, height = SwitchTrackHeight),
                        contentPadding = PaddingValues(0.dp),
                        shapes = appButtonShapes(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                    ) {
                        Text(quitLabel)
                    }
                }
            },
            expandedHeight = 54.4.dp,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            scrollBehavior = scrollBehavior
        )
    }
}
