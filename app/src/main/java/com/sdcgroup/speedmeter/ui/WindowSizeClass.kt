package com.sdcgroup.speedmeter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthSizeClass {
    ExtraCompact,
    Compact,
    Medium,
    Expanded,
    Large,
    ExtraLarge,
}

enum class WindowHeightSizeClass {
    Compact,
    Medium,
    Expanded,
}

fun Dp.toWindowWidthSizeClass(): WindowWidthSizeClass = when {
    this < 300.dp  -> WindowWidthSizeClass.ExtraCompact
    this < 600.dp  -> WindowWidthSizeClass.Compact
    this < 840.dp  -> WindowWidthSizeClass.Medium
    this < 1200.dp -> WindowWidthSizeClass.Expanded
    this < 1600.dp -> WindowWidthSizeClass.Large
    else           -> WindowWidthSizeClass.ExtraLarge
}

fun Dp.toWindowHeightSizeClass(): WindowHeightSizeClass = when {
    this < 480.dp  -> WindowHeightSizeClass.Compact
    this < 900.dp  -> WindowHeightSizeClass.Medium
    else           -> WindowHeightSizeClass.Expanded
}

@Composable
fun rememberWindowWidthSizeClass(): WindowWidthSizeClass {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenWidthDp) {
        configuration.screenWidthDp.dp.toWindowWidthSizeClass()
    }
}

@Composable
fun rememberWindowHeightSizeClass(): WindowHeightSizeClass {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenHeightDp) {
        configuration.screenHeightDp.dp.toWindowHeightSizeClass()
    }
}

@Composable
fun rememberWindowSizeClass(): Pair<WindowWidthSizeClass, WindowHeightSizeClass> {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenWidthDp, configuration.screenHeightDp) {
        configuration.screenWidthDp.dp.toWindowWidthSizeClass() to
        configuration.screenHeightDp.dp.toWindowHeightSizeClass()
    }
}
