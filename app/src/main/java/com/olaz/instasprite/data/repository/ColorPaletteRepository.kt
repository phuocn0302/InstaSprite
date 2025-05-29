package com.olaz.instasprite.data.repository

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.data.model.ColorPaletteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import androidx.core.graphics.toColorInt

class ColorPaletteRepository {
    suspend fun fetchPaletteFromLospec(paletteName: String): Result<ColorPaletteModel> = withContext(Dispatchers.IO) {
        try {
            val formattedName = paletteName.trim()
                .lowercase(Locale.getDefault())

            val url = URL("https://lospec.com/palette-list/$formattedName.json")
            val jsonString = url.readText()
            val jsonObject = JSONObject(jsonString)
            
            val name = jsonObject.getString("name")
            val author = jsonObject.getString("author")
            val colorsArray = jsonObject.getJSONArray("colors")
            val colors = List(colorsArray.length()) { colorsArray.getString(it) }
            
            Result.success(ColorPaletteModel(name, author, colors))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun convertHexToColor(hexColor: String): Color {
        return Color("#$hexColor".toColorInt())
    }

    fun convertPaletteToColors(palette: ColorPaletteModel): List<Color> {
        return palette.colors.map { convertHexToColor(it) }
    }
} 