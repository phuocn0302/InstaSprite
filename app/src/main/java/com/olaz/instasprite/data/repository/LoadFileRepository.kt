package com.olaz.instasprite.data.repository

import android.content.Context
import android.net.Uri
import com.olaz.instasprite.data.model.ISpriteData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

object LoadFileRepository {
    fun loadFile(context: Context, fileUri: Uri): ISpriteData? {
        return try {
            val inputStream = context.contentResolver.openInputStream(fileUri) ?: return null
            val jsonData = inputStream.bufferedReader().use { it.readText() }
            Json.decodeFromString<ISpriteData>(jsonData)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: SerializationException) {
            e.printStackTrace()
            null
        }
    }
}