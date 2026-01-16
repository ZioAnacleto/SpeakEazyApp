package com.zioanacleto.speakeazy.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel

@Entity
data class FavoriteEntity(
    @PrimaryKey val id: Int = 0,
    val name: String
)

fun FavoriteEntity.toDrinkModel() = DrinkModel(
    id = id.toString(),
    name = name
)