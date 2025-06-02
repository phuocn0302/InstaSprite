package com.olaz.instasprite.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import kotlinx.coroutines.flow.Flow

@Dao
interface SpriteDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sprite: ISpriteData)

    @Query("SELECT * FROM sprite_data WHERE id = :id")
    suspend fun getById(id: String): ISpriteData?

    @Query("SELECT * FROM sprite_data")
    suspend fun getAllSprites(): List<ISpriteData>

    @Transaction
    @Query("SELECT * FROM sprite_data")
    fun getAllSpritesWithMeta(): Flow<List<ISpriteWithMetaData>>

}