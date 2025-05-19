package com.olaz.instasprite.data.model

import androidx.compose.ui.graphics.Color

class PixelCanvasModel(val width: Int, val height: Int) {
    val pixels = MutableList(width * height) { Color.Transparent }

    fun setPixel(row: Int, col: Int, color: Color) {
        if (row in 0 until height && col in 0 until width) {
            pixels[row * width + col] = color
        }
    }

    fun getPixel(row: Int, col: Int): Color {
        return if (row in 0 until height && col in 0 until width) {
            pixels[row * width + col]
        } else {
            Color.Transparent
        }
    }
}
