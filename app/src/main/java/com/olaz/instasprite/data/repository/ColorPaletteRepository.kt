package com.olaz.instasprite.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.olaz.instasprite.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.ArrayDeque

class ColorPaletteRepository(private val context: Context) {

    private val _colors = MutableStateFlow(
        loadDefaultColorPalette(context)
    )
    val colors: StateFlow<List<Color>> = _colors.asStateFlow()

    private val _activeColor = MutableStateFlow(_colors.value.firstOrNull() ?: Color.Unspecified)
    val activeColor: StateFlow<Color> = _activeColor.asStateFlow()

    private val _recentColors = MutableStateFlow(ArrayDeque<Color> ())
    val recentColors: StateFlow<ArrayDeque<Color>> = _recentColors.asStateFlow()

    fun addColorToPalette(color: Color) {
        if (color !in _colors.value) {
            _colors.value = mutableListOf(color).apply { addAll(_colors.value) }
        }
    }

    fun setActiveColor(color: Color) {
        addColorToRecent(_activeColor.value)

        if (color !in _colors.value) {
            addColorToPalette(color)
        }
        _activeColor.value = color
    }

    fun updatePalette(colors: List<Color>) {
        if (colors.isNotEmpty()) {
            _colors.value = colors.toMutableList()
            setActiveColor(colors.first())
        }
    }

    private fun addColorToRecent(color: Color) {
        _recentColors.value.remove(color)
        _recentColors.value.addFirst(color)

        if (_recentColors.value.size > 100) {
            _recentColors.value.removeLast()
        }
    }
}

private fun loadDefaultColorPalette(context: Context): MutableList<Color> {
    val resourceId: Int = R.raw.sage57
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
                    } catch (_: IllegalArgumentException) {

                    }
                }
            }
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return ColorPaletteFallback.ColorsList.toMutableList()
    }
    return colors
}

// In case load default color fail, should not happen tho
private object ColorPaletteFallback {
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

    val ColorsList = listOf(
        Color1, Color2, Color3, Color4, Color5, Color6, Color7, Color8,
        Color9, Color10, Color11, Color12, Color13, Color14, Color15, Color16
    )
}