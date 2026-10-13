package com.sdcgroup.speedmeter.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WindowSizeDefaults(
    val horizontalPadding: Dp,
    val verticalPadding:   Dp,
    val contentMaxWidth:   Dp,
    val iconButtonSize:    Dp,
    val spacingUnit:       Dp,
)

fun windowSizeDefaultsFor(sizeClass: WindowWidthSizeClass): WindowSizeDefaults =
    when (sizeClass) {
        WindowWidthSizeClass.ExtraCompact -> WindowSizeDefaults(
            horizontalPadding = 8.dp,
            verticalPadding   = 4.dp,
            contentMaxWidth   = Dp.Infinity,
            iconButtonSize    = 36.dp,
            spacingUnit       = 4.dp,
        )
        WindowWidthSizeClass.Compact -> WindowSizeDefaults(
            horizontalPadding = 16.dp,
            verticalPadding   = 4.dp,
            contentMaxWidth   = Dp.Infinity,
            iconButtonSize    = 40.dp,
            spacingUnit       = 8.dp,
        )
        WindowWidthSizeClass.Medium -> WindowSizeDefaults(
            horizontalPadding = 32.dp,
            verticalPadding   = 8.dp,
            contentMaxWidth   = 840.dp,
            iconButtonSize    = 44.dp,
            spacingUnit       = 12.dp,
        )
        WindowWidthSizeClass.Expanded -> WindowSizeDefaults(
            horizontalPadding = 48.dp,
            verticalPadding   = 12.dp,
            contentMaxWidth   = 1000.dp,
            iconButtonSize    = 48.dp,
            spacingUnit       = 16.dp,
        )
        WindowWidthSizeClass.Large -> WindowSizeDefaults(
            horizontalPadding = 80.dp,
            verticalPadding   = 16.dp,
            contentMaxWidth   = 1200.dp,
            iconButtonSize    = 52.dp,
            spacingUnit       = 20.dp,
        )
        WindowWidthSizeClass.ExtraLarge -> WindowSizeDefaults(
            horizontalPadding = 120.dp,
            verticalPadding   = 24.dp,
            contentMaxWidth   = 1400.dp,
            iconButtonSize    = 56.dp,
            spacingUnit       = 24.dp,
        )
    }
