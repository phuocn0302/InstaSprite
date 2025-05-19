package com.olaz.instasprite.domain.tool

import com.olaz.instasprite.R

object PencilTool : Tool {
    override val icon: Int = R.drawable.ic_pencil_tool
    override val name: String = "Pencil"
    override val description: String = "Draw on the canvas"
    override fun apply(x: Int, y: Int) {

    }
}