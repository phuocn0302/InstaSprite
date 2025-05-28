package com.olaz.instasprite.utils

import android.graphics.Bitmap

object FileUtils {
    fun getFormatFromExtension(fileName: String): Pair<Bitmap.CompressFormat, String>? {
        return when (fileName.substringAfterLast('.', "").lowercase()) {
            "png" -> Bitmap.CompressFormat.PNG to "image/png"
            "jpg", "jpeg" -> Bitmap.CompressFormat.JPEG to "image/jpeg"
            "webp" -> Bitmap.CompressFormat.WEBP to "image/webp"
            else -> null
        }
    }
}
