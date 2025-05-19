package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.PixelCanvasModel

object PencilTool : Tool {
    override val icon: Int = R.drawable.ic_pencil_tool
    override val name: String = "Pencil"
    override val description: String = "Draw on the canvas"

    override fun apply(canvas: PixelCanvasModel, row: Int, col: Int, color: Color) {
        canvas.setPixel(row, col, color)
    }
}