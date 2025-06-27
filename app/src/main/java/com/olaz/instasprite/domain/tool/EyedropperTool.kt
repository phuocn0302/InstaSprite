package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.PixelCanvasModel

object EyedropperTool : Tool {
    override val icon: Int = R.drawable.ic_eyedropper_tool
    override val name: String = "Eyedropper"
    override val description: String = "Select a pixel color"
    override fun apply(canvas: PixelCanvasModel, row: Int, col: Int, color: Color) {
        val selectedColor = canvas.getPixel(row, col)
        if (selectedColor != Color.Transparent) {
            selectedColor
        } else {
            color
        }
    }
}