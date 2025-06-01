package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.domain.usecase.PixelCanvasUseCase

object EraserTool : Tool {
    override val icon: Int = R.drawable.ic_eraser_tool
    override val name: String = "Eraser"
    override val description: String = "Erase pixels on the canvas"

    override fun apply(canvas: PixelCanvasUseCase, x: Int, y: Int, color: Color) {
        canvas.setPixel(x, y, Color.Transparent)
    }
}