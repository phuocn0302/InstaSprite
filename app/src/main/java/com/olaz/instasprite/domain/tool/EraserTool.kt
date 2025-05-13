package com.olaz.instasprite.domain.tool

import com.olaz.instasprite.R

object EraserTool : Tool {
    override val icon: Int = R.drawable.ic_eraser_tool
    override val name: String = "Eraser"
    override val description: String = "Erase pixels on the canvas"

    override fun apply(x: Int, y: Int) {

    }
}