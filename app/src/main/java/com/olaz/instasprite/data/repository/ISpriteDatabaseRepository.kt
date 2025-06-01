package com.olaz.instasprite.data.repository

import android.util.Log
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.database.SpriteDataDao
import com.olaz.instasprite.data.database.SpriteMetaDataDao
import com.olaz.instasprite.data.model.SpriteMetaData

class ISpriteDatabaseRepository(
    private val dao: SpriteDataDao,
    private val metaDao: SpriteMetaDataDao
) {
    suspend fun saveSprite(sprite: ISpriteData) {
        Log.d("ISpriteDatabaseRepository", "Saving sprite: $sprite")
        val now = System.currentTimeMillis()

        val existingMeta = metaDao.getMetaById(sprite.id)

        val meta = existingMeta?.copy(lastModifiedAt = now) ?:
            SpriteMetaData(
                spriteId = sprite.id,
                createdAt = now,
                lastModifiedAt = now
            )

        metaDao.insert(meta)
        dao.insert(sprite)
    }

    suspend fun loadSprite(id: Int): ISpriteData? {
        return dao.getById(id)
    }
}