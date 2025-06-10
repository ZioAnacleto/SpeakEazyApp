package com.zioanacleto.speakeazy.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.Ingredient

@Entity
data class IngredientEntity(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val imageUrl: String
)

fun IngredientEntity.toIngredientModel() = Ingredient(
    id = id.toString(),
    name = name,
    imageUrl = imageUrl
)