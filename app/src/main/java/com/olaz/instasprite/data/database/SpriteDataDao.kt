package com.olaz.instasprite.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olaz.instasprite.data.model.ISpriteData

@Dao
interface SpriteDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sprite: ISpriteData)

    @Query("SELECT * FROM sprite_data WHERE id = :id")
    suspend fun getById(id: Int): ISpriteData?
}