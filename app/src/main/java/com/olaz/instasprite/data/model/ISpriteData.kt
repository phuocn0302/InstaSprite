package com.olaz.instasprite.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ISpriteData(
    val width: Int,
    val height: Int,
    val pixelsData: List<Int>,
    val colorPalette: List<Int>? = null
)