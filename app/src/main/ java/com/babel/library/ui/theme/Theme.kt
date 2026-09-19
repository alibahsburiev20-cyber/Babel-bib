package com.babel.library.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BabelDarkColorScheme = darkColorScheme(
    primary = BabelGold,
    onPrimary = BabelBackground,
    secondary = BabelGoldDim,
    onSecondary = BabelTextPrimary,
    background = BabelBackground,
    onBackground = BabelTextPrimary,
    surface = BabelSurface,
    onSurface = BabelTextPrimary,
    surfaceVariant = BabelSurfaceElevated,
    onSurfaceVariant = BabelTextSecondary,
    outline = BabelBorder,
    error = BabelError
)

@Composable
fun BabelLibraryTheme(
    // Библиотека всегда тёмная по замыслу дизайна — независимо от системной темы
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
            it.statusBarColor = BabelBackground.toArgb()
            it.navigationBarColor = BabelBackground.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = BabelDarkColorScheme,
        typography = BabelTypography,
        content = content
    )
}
