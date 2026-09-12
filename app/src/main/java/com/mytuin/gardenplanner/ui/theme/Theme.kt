package com.mytuin.gardenplanner.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Forest,
    onPrimary = Color.White,
    primaryContainer = Sage,
    onPrimaryContainer = DeepForest,
    secondary = Sage,
    onSecondary = Color.White,
    background = WarmWhite,
    onBackground = Charcoal,
    surface = WarmWhite,
    onSurface = Charcoal,
    surfaceVariant = Paper,
    onSurfaceVariant = Charcoal,
    outline = Stone,
    outlineVariant = Stone,
    error = Clay,
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Sage,
    onPrimary = DeepForest,
    primaryContainer = DeepForest,
    onPrimaryContainer = Sage,
    secondary = Sage,
    onSecondary = DeepForest,
    background = DarkSurface,
    onBackground = WarmWhite,
    surface = DarkSurface,
    onSurface = WarmWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = WarmWhite,
    outline = MidStone,
    outlineVariant = MidStone,
    error = Clay,
    onError = Color.White,
)

/**
 * V1_VISUAL_DESIGN_SPECIFICATION §53–§55: both themes supported.
 * Light is the default; dark follows the system.
 */
@Composable
fun MyTuinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MyTuinTypography,
        content = content
    )
}