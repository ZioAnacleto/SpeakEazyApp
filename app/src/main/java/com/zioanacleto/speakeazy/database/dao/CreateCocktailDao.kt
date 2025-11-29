package com.zioanacleto.speakeazy.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zioanacleto.speakeazy.database.entities.CreateCocktailEntity

// todo: should we delete old ones?

@Dao
interface CreateCocktailDao {
    @Query("SELECT * FROM createcocktailentity")
    fun getAll(): List<CreateCocktailEntity>

    @Upsert
    fun upsert(createCocktailEntity: CreateCocktailEntity): Long

    @Query("DELETE FROM createcocktailentity WHERE uniqueId = :uniqueId")
    fun delete(uniqueId: Int?)
}