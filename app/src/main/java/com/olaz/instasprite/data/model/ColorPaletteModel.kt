package com.olaz.instasprite.data.model

import androidx.compose.ui.graphics.Color

data class ColorPaletteModel(
    val name: String = "Unnamed",
    val author: String = "Anonymous",
    var colors: MutableList<Color> = mutableListOf(),
) 