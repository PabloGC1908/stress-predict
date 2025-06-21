package com.pgc.stresspredict.ui.theme

import android.app.Activity
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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),  // Verde principal
    secondary = Color(0xFF81C784), // Verde secundario
    tertiary = Color(0xFFA5D6A7),  // Verde terciario
    background = Color(0xFF121212), // Fondo oscuro
    surface = Color(0xFF1E1E1E),    // Superficie oscura
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,     // Texto blanco en fondo oscuro
    onSurface = Color.White         // Texto blanco en superficies oscuras
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),    // Verde principal
    secondary = Color(0xFF388E3C),  // Verde secundario más oscuro
    tertiary = Color(0xFF81C784),   // Verde terciario
    background = Color.White,       // Fondo blanco
    surface = Color.White,          // Superficie blanca
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,     // Texto negro en fondo claro
    onSurface = Color.Black         // Texto negro en superficies claras
)

@Composable
fun StressPredictTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado por defecto para mantener consistencia
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}