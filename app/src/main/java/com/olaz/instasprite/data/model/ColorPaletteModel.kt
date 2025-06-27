package com.olaz.instasprite.data.model

import androidx.compose.ui.graphics.Color

data class ColorPaletteModel(
    val name: String,
    val author: String,
    val colors: List<Color>,
    val activeColor: Color = colors.first()
) 