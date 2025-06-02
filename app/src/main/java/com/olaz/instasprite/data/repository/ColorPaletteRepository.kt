package com.olaz.instasprite.data.repository

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.data.model.ColorPaletteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import androidx.core.graphics.toColorInt

class ColorPaletteRepository {
//    suspend fun fetchPaletteFromLospec(paletteName: String): Result<ColorPaletteModel> = withContext(Dispatchers.IO) {
//        try {
//            val formattedName = paletteName.trim()
//                .lowercase(Locale.getDefault())
//
//            val url = URL("https://lospec.com/palette-list/$formattedName.json")
//            val jsonString = url.readText()
//            val jsonObject = JSONObject(jsonString)
//
//            val name = jsonObject.getString("name")
//            val author = jsonObject.getString("author")
//            val colorsArray = jsonObject.getJSONArray("colors")
//            val colors = List(colorsArray.length()) { colorsArray.getString(it) }
//
//            Result.success(ColorPaletteModel(name, author, colors))
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

    fun convertHexToColor(hexColor: String): Color {
        return Color("#$hexColor".toColorInt())
    }

//    fun convertPaletteToColors(palette: ColorPaletteModel): List<Color> {
//        return palette.colors.map { convertHexToColor(it) }
//    }

    suspend fun fetchPaletteFromUrl(url: String): Result<List<Color>> = withContext(Dispatchers.IO) {
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
            
            Result.success(colors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importPaletteFromFile(context: Context, uri: Uri): Result<List<Color>> = withContext(Dispatchers.IO) {
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
                Result.success(colors)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    private fun parseColorsFromJson(jsonString: String): List<Color> {
//        return emptyList()
//    }
} 