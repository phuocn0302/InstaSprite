package com.olaz.instasprite.data.repository

import android.util.Log
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.database.SpriteDataDao
import com.olaz.instasprite.data.database.SpriteMetaDataDao
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.model.SpriteMetaData
import kotlinx.coroutines.flow.Flow

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

        dao.insert(sprite)
        metaDao.insert(meta)
    }

    suspend fun loadSprite(id: String): ISpriteData? {
        return dao.getById(id)
    }

    suspend fun getSpriteList(): Pair<List<ISpriteData>, List<SpriteMetaData>> {
        return Pair(dao.getAllSprites(), metaDao.getAllMeta())
    }

    fun getAllSpritesWithMeta(): Flow<List<ISpriteWithMetaData>> {
        return dao.getAllSpritesWithMeta()
    }

}