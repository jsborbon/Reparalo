package com.jsborbon.reparalo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = Blue80,
        secondary = BlueGrey80,
        tertiary = Teal80,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onTertiary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Blue40,
        secondary = BlueGrey40,
        tertiary = Teal40,
        background = Color(0xFFF5F5F5),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
    )

@Composable
fun ReparaloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Desactivamos colores dinámicos para usar nuestra paleta personalizada
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    // Aplicamos colores personalizados para una app de reparaciones
    val customColorScheme = colorScheme.copy(
        primary = if (darkTheme) PrimaryDark else PrimaryLight,
        secondary = Secondary,
        tertiary = Accent,
        background = if (darkTheme) BackgroundDark else BackgroundLight,
        surface = if (darkTheme) SurfaceDark else SurfaceLight,
        error = Error,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.Black,
        surfaceTint = if (darkTheme) PrimaryDark.copy(alpha = 0.1f) else PrimaryLight.copy(alpha = 0.1f),
    )

    // Configuramos la barra de estado para que coincida con nuestro tema
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = customColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = Typography,
        content = content,
    )
}
