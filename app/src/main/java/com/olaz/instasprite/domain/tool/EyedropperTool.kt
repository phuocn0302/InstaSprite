package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.domain.usecase.PixelCanvasUseCase

object EyedropperTool : Tool {
    override val icon: Int = R.drawable.ic_eyedropper_tool
    override val name: String = "Eyedropper"
    override val description: String = "Select a pixel color"
    override fun apply(canvas: PixelCanvasUseCase, row: Int, col: Int, color: Color) {
        val selectedColor = canvas.getPixel(row, col)
        if (selectedColor != Color.Transparent) {
            selectedColor
        } else {
            color
        }
    }
}