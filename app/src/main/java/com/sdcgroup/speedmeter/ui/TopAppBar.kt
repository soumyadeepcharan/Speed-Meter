package com.sdcgroup.speedmeter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
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
    expansion: Float = 0f,
    modifier: Modifier = Modifier
) {
    val quitLabel = stringResource(id = R.string.quit_button_label)
    val density = LocalDensity.current
    val baseHeightPx = with(density) { 54.4.dp.toPx() }
    val statusInsetPx = WindowInsets.statusBars.getTop(density)
    val targetHeightPx = LocalWindowInfo.current.containerSize.height / 3f
    val extraPx = (targetHeightPx - statusInsetPx - baseHeightPx).coerceAtLeast(0f) * expansion
    val expandedHeight = with(density) { (baseHeightPx + extraPx).toDp() }
    val titleOutAlpha = (1f - expansion * 2f).coerceIn(0f, 1f)
    val titleInProgress = ((expansion - 0.5f) * 2f).coerceIn(0f, 1f)
    val titleShiftPx = with(density) { 24.dp.toPx() }
    key(themeMode) {
        Box {
        TopAppBar(
            title = {
                Text(
                    title,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .offset { IntOffset(0, -(extraPx / 2f).roundToInt()) }
                        .graphicsLayer { alpha = titleOutAlpha }
                )
            },
            modifier = modifier,
            actions = {
                Box(
                    modifier = Modifier.layout { measurable, _ ->
                        val placeable = measurable.measure(androidx.compose.ui.unit.Constraints())
                        val reserved = maxOf(108.dp.roundToPx(), placeable.width)
                        layout(reserved, placeable.height) {
                            placeable.place((reserved - placeable.width) / 2, 0)
                        }
                    }.offset { IntOffset(0, (extraPx / 2f).roundToInt()) }
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
            expandedHeight = expandedHeight,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            scrollBehavior = scrollBehavior
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.graphicsLayer {
                    alpha = titleInProgress
                    translationY = (titleInProgress - 1f) * titleShiftPx
                }
            )
        }
        }
    }
}
