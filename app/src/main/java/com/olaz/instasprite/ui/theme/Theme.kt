package com.olaz.instasprite.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun InstaSpriteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object DrawingScreenColor {
    // UI Colors
    val BackgroundColor = Color(0xFF1A1A2E)
    val PaletteBarColor = Color(0xFF262B44)
    val PaletteBackgroundColor = Color(0xFF5A6988)
    val CanvasBorderColor = Color(0xFF2D3250)
    val SelectedToolColor = Color(0xFF4D5881)
    val ColorItemBorder = Color(0xFF262B44)
    val ColorItemBorderOverlay = Color.White.copy(alpha = 0.3f)

    // Canvas Colors
    val DefaultCanvasColor = Color(0xFF9BAFD9)
    val CheckerColor1 = Color(0xFF808080)
    val CheckerColor2 = Color(0xFFC0C0C0)
}

object HomeScreenColor {
    val BackgroundColor = Color(0xFF1A1A2E)
    val SelectedColor = Color(0xFF4D5881)
}