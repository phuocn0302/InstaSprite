package com.olaz.instasprite.data.model

import androidx.compose.ui.graphics.Color

class PixelCanvas(val width: Int, val height: Int) {
    val pixels = MutableList(width * height) { Color.Transparent }

    fun setPixel(x: Int, y: Int, color: Color) {
        if (x in 0 until width && y in 0 until height) {
            pixels[y * width + x] = color
        }
    }

    fun getPixel(x: Int, y: Int): Color {
        return if (x in 0 until width && y in 0 until height) {
            pixels[y * width + x]
        } else {
            Color.Transparent
        }
    }
}
