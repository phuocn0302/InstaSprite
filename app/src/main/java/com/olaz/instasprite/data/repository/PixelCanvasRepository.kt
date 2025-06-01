package com.olaz.instasprite.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.PixelCanvasModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PixelCanvasRepository(var model: PixelCanvasModel) {
    var width: Int
        get() = model.width
        set(value) { model.width = value }

    var height: Int
        get() = model.height
        set(value) { model.height = value }

    private var pixels = MutableList(width * height){ Color.Transparent }

    private val _pixelChanged = MutableStateFlow(0L)
    val pixelChanged = _pixelChanged.asStateFlow()

    fun setPixel(row: Int, col: Int, color: Color) {
        if (row in 0 until height && col in 0 until width) {
            pixels[row * width + col] = color
            updatePixelChangedTrigger()
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
            updatePixelChangedTrigger()
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
        updatePixelChangedTrigger()
    }

    fun getISpriteData(): ISpriteData {
        return ISpriteData(width, height, pixels.map { it.toArgb() })
    }

    private fun updatePixelChangedTrigger() {
        _pixelChanged.value = System.nanoTime()
    }
}