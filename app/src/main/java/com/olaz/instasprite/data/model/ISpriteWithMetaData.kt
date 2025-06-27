package com.olaz.instasprite.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ISpriteWithMetaData(
    @Embedded val sprite: ISpriteData,

    @Relation(
        parentColumn = "id",
        entityColumn = "spriteId"
    )
    val meta: SpriteMetaData?
)