package com.olaz.instasprite.data.repository

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.data.model.ColorPaletteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import com.olaz.instasprite.utils.convertHexToColor

class ColorPaletteRepository(private val colorPaletteModel: ColorPaletteModel) {

    suspend fun fetchPaletteFromUrl(url: String): Result<ColorPaletteModel> = withContext(Dispatchers.IO) {
        try {

            val paletteName = when {
                url.contains("/palette-list/") -> {
                    url.substringAfterLast("/palette-list/")
                        .substringBefore(".json")
                        .substringBefore("/")
                        .trim()
                }
                url.contains("lospec.com") -> {
                    url.substringAfterLast("/")
                        .substringBefore("?")
                        .trim()
                }
                else -> {
                    return@withContext Result.failure(Exception("Invalid Lospec URL"))
                }
            }

            val jsonUrl = URL("https://lospec.com/palette-list/$paletteName.json")
            val jsonString = jsonUrl.readText()
            val jsonObject = JSONObject(jsonString)
            
            val colorsArray = jsonObject.getJSONArray("colors")
            val colors = List(colorsArray.length()) { 
                convertHexToColor(colorsArray.getString(it))
            }

            val paletteNameFromJson = jsonObject.optString("name", paletteName)
            val author = jsonObject.optString("author", "Unknown")
            
            val updatedPalette = colorPaletteModel.copy(
                name = paletteNameFromJson,
                author = author,
                colors = colors
            )
            
            Result.success(updatedPalette)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importPaletteFromFile(context: Context, uri: Uri): Result<ColorPaletteModel> = withContext(Dispatchers.IO) {
        try {
            val colors = mutableListOf<Color>()
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        try {

                            if (!line.startsWith(";") && line.isNotBlank()) {

                                val cleanHex = when {
                                    line.startsWith("FF") -> line.substring(2)
                                    else -> line.trim().removePrefix("#")
                                }
                                
                                if (cleanHex.length == 6) {
                                    colors.add(convertHexToColor(cleanHex))
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }
                }
            }
            
            if (colors.isEmpty()) {
                Result.failure(Exception("No valid colors found in file"))
            } else {
                val updatedPalette = colorPaletteModel.copy(
                    name = "Imported Palette",
                    author = "File Import",
                    colors = colors
                )
                Result.success(updatedPalette)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun addColorToPalette(color: Color): ColorPaletteModel {
        val updatedColors = listOf(color) + colorPaletteModel.colors
        return colorPaletteModel.copy(colors = updatedColors)
    }

    fun removeColorFromPalette(color: Color): ColorPaletteModel {
        val updatedColors = colorPaletteModel.colors.filter { it != color }
        return colorPaletteModel.copy(colors = updatedColors)
    }

    fun clearPalette(): ColorPaletteModel {
        return colorPaletteModel.copy(colors = emptyList())
    }

    fun getCurrentPalette(): ColorPaletteModel {
        return colorPaletteModel
    }
} 