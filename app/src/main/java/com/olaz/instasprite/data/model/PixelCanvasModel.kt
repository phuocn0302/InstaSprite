package com.olaz.instasprite.data.model

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PixelCanvasModel(val width: Int, val height: Int) {
    val pixels = MutableList(width * height) { Color.Transparent }

    private val _pixelChanged = MutableStateFlow(0)
    val pixelChanged = _pixelChanged.asStateFlow()

    fun setPixel(row: Int, col: Int, color: Color) {
        if (row in 0 until height && col in 0 until width) {
            pixels[row * width + col] = color
            _pixelChanged.value = (_pixelChanged.value + 1) % 1000
        }
    }

    fun getPixel(row: Int, col: Int): Color {
        return if (row in 0 until height && col in 0 until width) {
            pixels[row * width + col]
        } else {
            Color.Transparent
        }
    }

    fun getAllPixels(): List<Color> {
        return pixels.toList()
    }

    fun setAllPixels(colors: List<Color>) {
        if (colors.size == pixels.size) {
            colors.forEachIndexed { index, color ->
                pixels[index] = color
            }
            _pixelChanged.value = (_pixelChanged.value + 1) % 1000
        }
    }

}
