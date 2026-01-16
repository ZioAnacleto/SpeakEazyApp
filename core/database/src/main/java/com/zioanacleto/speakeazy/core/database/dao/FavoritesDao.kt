package com.zioanacleto.speakeazy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zioanacleto.speakeazy.core.database.entities.FavoriteEntity

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favoriteentity")
    fun getAll(): List<FavoriteEntity>

    @Query("SELECT * FROM favoriteentity WHERE id IN (:favoriteIds)")
    fun loadAllByIds(favoriteIds: IntArray): List<FavoriteEntity>

    @Insert
    fun insert(vararg favorites: FavoriteEntity)

    @Query("DELETE FROM favoriteentity WHERE id = :favoriteId")
    fun delete(favoriteId: Int)
}