package com.olaz.instasprite.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.R

val RetronFont = FontFamily(
    Font(R.font.svn_retron, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RetronFont
    ),
    displayMedium = TextStyle(
        fontFamily = RetronFont
    ),
    displaySmall = TextStyle(
        fontFamily = RetronFont
    ),
    headlineLarge = TextStyle(
        fontFamily = RetronFont
    ),
    headlineMedium = TextStyle(
        fontFamily = RetronFont
    ),
    headlineSmall = TextStyle(
        fontFamily = RetronFont
    ),
    titleLarge = TextStyle(
        fontFamily = RetronFont,
    ),
    titleMedium = TextStyle(
        fontFamily = RetronFont,
    ),
    titleSmall = TextStyle(
        fontFamily = RetronFont,
    ),
    bodyLarge = TextStyle(
        fontFamily = RetronFont,
    ),
    bodyMedium = TextStyle(
        fontFamily = RetronFont,
    ),
    bodySmall = TextStyle(
        fontFamily = RetronFont
    ),
    labelLarge = TextStyle(
        fontFamily = RetronFont,
    ),
    labelMedium = TextStyle(
        fontFamily = RetronFont,
    ),
    labelSmall = TextStyle(
        fontFamily = RetronFont,
    )
)