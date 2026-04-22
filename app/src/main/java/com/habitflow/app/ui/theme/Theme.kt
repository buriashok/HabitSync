package com.habitflow.newapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = Color.White,
    secondary = Purple500,
    onSecondary = Color.White,
    tertiary = Cyan500,
    background = DarkBgPrimary,
    onBackground = DarkTextPrimary,
    surface = DarkBgCard,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkBgSecondary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorderColor,
    error = Red500,
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo500,
    onPrimary = Color.White,
    secondary = Purple500,
    onSecondary = Color.White,
    tertiary = Cyan500,
    background = LightBgPrimary,
    onBackground = LightTextPrimary,
    surface = LightBgCard,
    onSurface = LightTextPrimary,
    surfaceVariant = LightBgSecondary,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorderColor,
    error = Red500,
    onError = Color.White,
)

// Extended colors for custom usage
data class ExtendedColors(
    val bgPrimary: Color,
    val bgSecondary: Color,
    val bgCard: Color,
    val bgCardHover: Color,
    val bgNav: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderColor: Color,
    val accent: Color,
    val accentLight: Color,
    val accentBg: Color,
    val success: Color,
    val warning: Color,
    val danger: Color,
)

val DarkExtendedColors = ExtendedColors(
    bgPrimary = DarkBgPrimary,
    bgSecondary = DarkBgSecondary,
    bgCard = DarkBgCard,
    bgCardHover = DarkBgCardHover,
    bgNav = DarkBgNav,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textMuted = DarkTextMuted,
    borderColor = DarkBorderColor,
    accent = Indigo500,
    accentLight = Indigo400,
    accentBg = Indigo500.copy(alpha = 0.15f),
    success = Green500,
    warning = Yellow500,
    danger = Red500,
)

val LightExtendedColors = ExtendedColors(
    bgPrimary = LightBgPrimary,
    bgSecondary = LightBgSecondary,
    bgCard = LightBgCard,
    bgCardHover = LightBgCardHover,
    bgNav = LightBgNav,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textMuted = LightTextMuted,
    borderColor = LightBorderColor,
    accent = Indigo500,
    accentLight = Indigo400,
    accentBg = Indigo500.copy(alpha = 0.1f),
    success = Green500,
    warning = Yellow500,
    danger = Red500,
)

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }
val LocalIsDarkTheme = staticCompositionLocalOf { true }
val LocalToggleTheme = staticCompositionLocalOf<() -> Unit> { {} }

object AppTheme {
    val colors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current

    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkTheme.current
}

@Composable
fun HabitFlowTheme(
    content: @Composable () -> Unit
) {
    var isDark by remember { mutableStateOf(true) }
    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme
    val extendedColors = if (isDark) DarkExtendedColors else LightExtendedColors

    val toggleTheme = { isDark = !isDark }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalIsDarkTheme provides isDark,
        LocalToggleTheme provides toggleTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = HabitFlowTypography,
            content = content
        )
    }
}
