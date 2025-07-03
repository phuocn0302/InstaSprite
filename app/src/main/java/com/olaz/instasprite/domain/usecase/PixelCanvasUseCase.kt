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
        val width = pixelCanvasRepository.width
        val height = pixelCanvasRepository.height
        val rotatedPixels = mutableListOf<Color>()

        for (row in 0 until height) {
            for (col in 0 until width) {
                val newRow = height - 1 - col
                val newCol = row

                if (newRow >= 0 && newRow < height && newCol < width) {
                    val index = newRow * width + newCol
                    if (index < pixels.size) {
                        rotatedPixels.add(pixels[index])
                    } else {
                        rotatedPixels.add(Color.Transparent)
                    }
                } else {
                    rotatedPixels.add(Color.Transparent)
                }
            }
        }

        return setAllPixels(rotatedPixels)
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