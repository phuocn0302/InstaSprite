package com.olaz.instasprite.domain.tool

interface Tool {
    val icon: Int
    val name: String
    val description: String
    fun apply(x: Int, y: Int) : Unit
}