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

    companion object {
        fun createDefaultPalette(context: Context): ColorPaletteModel {
            val colors = mutableListOf<Color>()
            
            try {
                context.resources.openRawResource(com.olaz.instasprite.R.raw.sage57).use { inputStream ->
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
            } catch (e: Exception) {
  
                colors.addAll(listOf(
                    Color.Black,
                    Color(0xFF1D2B53),
                    Color(0xFF7e2553),
                    Color(0xFF008751),
                    Color(0xFFab5236),
                    Color(0xFF5f574f),
                    Color(0xFFc2c3c7),
                    Color(0xFFfff1e8),
                    Color(0xFFff004d),
                    Color(0xFFffa300),
                    Color(0xFFffec27),
                    Color(0xFF00e436),
                    Color(0xFF29adff),
                    Color(0xFF83769c),
                    Color(0xFFff77a8),
                    Color(0xFFffccaa),
                ))
            }
            
            return ColorPaletteModel(
                name = "SAGE57",
                author = "Lospec",
                colors = colors,
            )
        }
    }

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

//    fun addColorToPalette(color: Color): ColorPaletteModel {
//        val updatedColors = listOf(color) + colorPaletteModel.colors
//        return colorPaletteModel.copy(colors = updatedColors)
//    }
//
//    fun setActiveColor(color: Color): ColorPaletteModel {
//        return colorPaletteModel.copy(activeColor = color)
//    }
//
//    fun getActiveColor(): Color {
//        return colorPaletteModel.activeColor
//    }
//
//    fun loadDefaultPalette(context: Context): ColorPaletteModel {
//        return createDefaultPalette(context)
//    }
} 