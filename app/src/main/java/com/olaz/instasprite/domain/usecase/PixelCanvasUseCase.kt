package com.olaz.instasprite.domain.usecase

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.repository.PixelCanvasRepository

class PixelCanvasUseCase(private val repo: PixelCanvasRepository) {

    fun getCanvasWidth(): Int {
        return repo.width
    }

    fun getCanvasHeight(): Int {
        return repo.height
    }

    fun setPixel(row: Int, col: Int, color: Color) {
        repo.setPixel(row, col, color)
    }

    fun getPixel(row: Int, col: Int): Color {
        return repo.getPixel(row, col)
    }

    fun setCanvas(width: Int, height: Int, pixels: List<Color>? = null) {
        repo.setCanvas(width, height, pixels)
    }

    fun setCanvas(spriteData: ISpriteData) {
        val decodedPixels = spriteData.pixelsData.map { Color(it) }
        setCanvas(spriteData.width, spriteData.height, decodedPixels)
    }

    fun setAllPixels(pixels: List<Color>) {
        repo.setAllPixels(pixels)
    }

    fun getAllPixels(): List<Color> {
        return repo.getAllPixels()
    }

    fun getISpriteData(): ISpriteData {
        return repo.getISpriteData()
    }

    fun rotateCanvas(pixels: List<Color>) {
        val width = repo.width
        val height = repo.height
        val rotatedPixels = mutableListOf<Color>()

        for (row in 0 until height) {
            for (col in 0 until width) {
                val newRow = height - 1 - col
                val newCol = row

                if (newRow >= 0 && newRow < height && newCol < width) {
                    val Index = newRow * width + newCol
                    if (Index < pixels.size) {
                        rotatedPixels.add(pixels[Index])
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
        val width = repo.width
        val height = repo.height
        val flippedPixels = mutableListOf<Color>()

        for (row in 0 until height) {
            for (col in width - 1 downTo 0) {
                flippedPixels.add(pixels[row * width + col])
            }
        }
        return setAllPixels(flippedPixels)
    }

    fun vFlipCanvas(pixels: List<Color>) {
        val width = repo.width
        val height = repo.height
        val flippedPixels = mutableListOf<Color>()

        for (row in height - 1 downTo 0) {
            for (col in 0 until width) {
                flippedPixels.add(pixels[row * width + col])
            }
        }

        return setAllPixels(flippedPixels)
    }

    val pixelChanged = repo.pixelChanged
}