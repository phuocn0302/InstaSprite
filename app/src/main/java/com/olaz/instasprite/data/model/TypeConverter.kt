package com.olaz.instasprite.data.model

import androidx.room.TypeConverter

class IntListConverter {

    @TypeConverter
    fun fromIntList(list: List<Int>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(data: String?): List<Int>? {
        return data?.split(",")?.mapNotNull { it.toIntOrNull() }
    }
}
