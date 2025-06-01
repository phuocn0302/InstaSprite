package com.olaz.instasprite.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sprite_metadata")
data class SpriteMetaData(
    @PrimaryKey val spriteId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis()
)
