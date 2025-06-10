package com.zioanacleto.speakeazy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zioanacleto.speakeazy.database.entities.IngredientEntity

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingrediententity")
    fun getAll(): List<IngredientEntity>

    @Query("SELECT * FROM ingrediententity WHERE id IN (:ingredientIds)")
    fun loadAllByIds(ingredientIds: IntArray): List<IngredientEntity>

    @Insert
    fun insertAll(vararg ingredients: IngredientEntity)
}