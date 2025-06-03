package com.olaz.instasprite.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.core.graphics.toColorInt
import com.olaz.instasprite.R

object ColorPalette {
    val Color1 = Color.Black
    val Color2 = Color(0xFF1D2B53)
    val Color3 = Color(0xFF7e2553)
    val Color4 = Color(0xFF008751)
    val Color5 = Color(0xFFab5236)
    val Color6 = Color(0xFF5f574f)
    val Color7 = Color(0xFFc2c3c7)
    val Color8 = Color(0xFFfff1e8)

    val Color9 = Color(0xFFff004d)
    val Color10 = Color(0xFFffa300)
    val Color11 = Color(0xFFffec27)
    val Color12 = Color(0xFF00e436)
    val Color13 = Color(0xFF29adff)
    val Color14 = Color(0xFF83769c)
    val Color15 = Color(0xFFff77a8)
    val Color16 = Color(0xFFffccaa)

    // Color palette as a list for easy access
    val ColorsList = listOf(
        Color1, Color2, Color3, Color4, Color5, Color6, Color7, Color8,
        Color9, Color10, Color11, Color12, Color13, Color14, Color15, Color16
    )

    var activeColor = Color1

    private var _colorsList = ColorsList.toMutableList()
    
    fun addColorToStart(color: Color) {
        if (!_colorsList.contains(color)) {
            _colorsList.add(0, color)
        }
    }
    
    fun getColors(): List<Color> = _colorsList.toList()
}

fun loadColorsFromFile(context: Context, resourceId: Int = R.raw.sage57): List<Color> {
    val colors = mutableListOf<Color>()
    try {
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            line?.trim()?.let { hexColor ->
                if (hexColor.isNotEmpty()) {
                    try {
                        // Assuming hexColor is like "RRGGBB" or "AARRGGBB"
                        val colorValue = "#$hexColor".toColorInt()
                        colors.add(Color(colorValue))
                    } catch (e: IllegalArgumentException) {
                        println("Warning: Invalid color format in file: $hexColor")
                    }
                }
            }
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return getDefaultColors()
    }
    return colors
}

fun getDefaultColors(): List<Color> {
    return ColorPalette.ColorsList
}