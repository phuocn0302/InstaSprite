package com.olaz.instasprite.domain.usecase

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.repository.PixelCanvasRepository
import com.olaz.instasprite.data.repository.ColorPaletteRepository

class PixelCanvasUseCase(
    private val pixelCanvasRepository: PixelCanvasRepository,
    private val colorPaletteRepository: ColorPaletteRepository,
) {

    fun getCanvasWidth(): Int {
        return pixelCanvasRepository.width
    }

    fun getCanvasHeight(): Int {
        return pixelCanvasRepository.height
    }

    fun setPixel(row: Int, col: Int, color: Color) {
        pixelCanvasRepository.setPixel(row, col, color)
    }

    fun getPixel(row: Int, col: Int): Color {
        return pixelCanvasRepository.getPixel(row, col)
    }

    fun setCanvas(width: Int, height: Int, pixels: List<Color>? = null) {
        pixelCanvasRepository.setCanvas(width, height, pixels)
    }

    fun setCanvas(spriteData: ISpriteData) {
        val decodedPixels = spriteData.pixelsData.map { Color(it) }
        setCanvas(spriteData.width, spriteData.height, decodedPixels)
    }

    fun setAllPixels(pixels: List<Color>) {
        pixelCanvasRepository.setAllPixels(pixels)
    }

    fun getAllPixels(): List<Color> {
        return pixelCanvasRepository.getAllPixels()
    }

    fun getISpriteData(): ISpriteData {
        return pixelCanvasRepository.getISpriteData()
    }

    fun selectColor(color: Color) {
        colorPaletteRepository.setActiveColor(color)
    }

    fun rotateCanvas(pixels: List<Color>) {
        val oldWidth = pixelCanvasRepository.width
        val oldHeight = pixelCanvasRepository.height
        val rotatedPixels = MutableList(pixels.size) { Color.Transparent }

        for (row in 0 until oldHeight) {
            for (col in 0 until oldWidth) {
                val oldIndex = row * oldWidth + col
                val newRow = col
                val newCol = oldHeight - 1 - row
                val newIndex = newRow * oldHeight + newCol
                if (newIndex in rotatedPixels.indices && oldIndex in pixels.indices) {
                    rotatedPixels[newIndex] = pixels[oldIndex]
                }
            }
        }

        return pixelCanvasRepository.setCanvas(oldHeight, oldWidth, rotatedPixels)
    }

    fun hFlipCanvas(pixels: List<Color>) {
        val width = pixelCanvasRepository.width
        val height = pixelCanvasRepository.height
        val flippedPixels = mutableListOf<Color>()

        for (row in 0 until height) {
            for (col in width - 1 downTo 0) {
                flippedPixels.add(pixels[row * width + col])
            }
        }
        return setAllPixels(flippedPixels)
    }

    fun vFlipCanvas(pixels: List<Color>) {
        val width = pixelCanvasRepository.width
        val height = pixelCanvasRepository.height
        val flippedPixels = mutableListOf<Color>()

        for (row in height - 1 downTo 0) {
            for (col in 0 until width) {
                flippedPixels.add(pixels[row * width + col])
            }
        }

        return setAllPixels(flippedPixels)
    }

    val pixelChanged = pixelCanvasRepository.pixelChanged
}