package com.olaz.instasprite.data.model

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PixelCanvasModel(var width: Int, var height: Int) {
    var pixels = MutableList(width * height) { Color.Transparent }

    private val _pixelChanged = MutableStateFlow(0)
    val pixelChanged = _pixelChanged.asStateFlow()

    fun setPixel(row: Int, col: Int, color: Color) {
        if (row in 0 until height && col in 0 until width) {
            pixels[row * width + col] = color
            _pixelChanged.value = System.currentTimeMillis().toInt()
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
            _pixelChanged.value = System.currentTimeMillis().toInt()
        }
    }

    fun setCanvas(width: Int, height: Int, pixelsData: List<Color>? = null) {
        this.width = width
        this.height = height
        this.pixels = if (pixelsData != null && pixelsData.size == width * height) {
            pixelsData.toMutableList()
        } else {
            MutableList(width * height) { Color.Transparent }
        }
        _pixelChanged.value = System.currentTimeMillis().toInt()
    }
}
