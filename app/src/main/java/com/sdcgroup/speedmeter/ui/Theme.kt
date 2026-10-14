package com.sdcgroup.speedmeter.ui

import android.app.Activity
import android.os.Build
import android.view.RoundedCorner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.sdcgroup.speedmeter.data.ThemeMode

private val DefaultCornerRadius = 28.dp

@Composable
internal fun rememberDeviceCornerRadius(): Dp {
    val view = LocalView.current
    val density = LocalDensity.current
    return remember(view, density) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val display = view.display
            val positions = listOf(
                RoundedCorner.POSITION_TOP_LEFT,
                RoundedCorner.POSITION_TOP_RIGHT,
                RoundedCorner.POSITION_BOTTOM_LEFT,
                RoundedCorner.POSITION_BOTTOM_RIGHT
            )
            val radiusPx = positions
                .mapNotNull { display?.getRoundedCorner(it)?.radius }
                .maxOrNull()
            if (radiusPx != null && radiusPx > 0) {
                with(density) { radiusPx.toDp() }
            } else {
                DefaultCornerRadius
            }
        } else {
            DefaultCornerRadius
        }
    }
}

@Composable
fun MyComposeApplicationTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val cornerShape = RoundedCornerShape(rememberDeviceCornerRadius())
    val shapes = Shapes(
        extraSmall = cornerShape,
        small = cornerShape,
        medium = cornerShape,
        large = cornerShape,
        extraLarge = cornerShape
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
