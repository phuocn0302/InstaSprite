package com.olaz.instasprite.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt


fun convertHexToColor(hexColor: String): Color {
    return Color("#$hexColor".toColorInt())
}

