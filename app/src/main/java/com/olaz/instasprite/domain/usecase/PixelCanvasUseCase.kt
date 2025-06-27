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

    val pixelChanged = repo.pixelChanged
}