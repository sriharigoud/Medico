package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MedicoPrimary,
    onPrimary = MedicoOnPrimary,
    primaryContainer = MedicoPrimaryContainer,
    onPrimaryContainer = MedicoOnPrimary,
    secondary = MedicoGreenDark,
    onSecondary = MedicoOnPrimary,
    secondaryContainer = MedicoGreenContainer,
    onSecondaryContainer = MedicoGreenDark,
    tertiary = MedicoTertiary,
    background = MedicoBackground,
    onBackground = MedicoOnSurface,
    surface = MedicoSurface,
    onSurface = MedicoOnSurface,
    surfaceVariant = MedicoSurfaceContainerHigh,
    onSurfaceVariant = MedicoOnSurfaceVariant,
    outline = MedicoOutline,
    outlineVariant = MedicoOutlineVariant,
    error = MedicoError,
    onError = MedicoOnPrimary,
    errorContainer = MedicoErrorContainer
)

@Composable
fun MedicoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = MedicoBackground.toArgb()
            window.navigationBarColor = MedicoBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
