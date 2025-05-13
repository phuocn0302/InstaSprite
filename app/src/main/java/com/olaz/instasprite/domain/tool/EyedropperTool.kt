package com.olaz.instasprite.domain.tool

import com.olaz.instasprite.R

object EyedropperTool : Tool {
    override val icon: Int = R.drawable.ic_eyedropper_tool
    override val name: String = "Eyedropper"
    override val description: String = "Select a pixel color"
    override fun apply(x: Int, y: Int) {

    }
}