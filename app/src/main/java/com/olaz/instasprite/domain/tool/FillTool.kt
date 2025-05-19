package com.olaz.instasprite.domain.tool

import com.olaz.instasprite.R

object FillTool : Tool {
    override val icon: Int = R.drawable.ic_fill_tool
    override val name: String = "Fill"
    override val description: String = "Fill canvas section with the selected color"
    override fun apply(x: Int, y: Int) {

    }
}