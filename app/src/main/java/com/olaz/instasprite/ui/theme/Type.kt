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
        fontFamily = RetronFont,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = RetronFont,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = RetronFont,
        fontSize = 36.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = RetronFont,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RetronFont,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = RetronFont,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RetronFont,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RetronFont,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = RetronFont,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RetronFont,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RetronFont,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = RetronFont,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RetronFont,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RetronFont,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RetronFont,
        fontSize = 11.sp
    )
)
