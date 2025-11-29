package com.zioanacleto.speakeazy.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import java.util.Date

@Entity
data class CreateCocktailEntity(
    @PrimaryKey(autoGenerate = true) val uniqueId: Int? = null,
    val currentStep: CreateWizardStepData,
    val createdTime: Long, // since we provide converters, maybe this should still be a Date
    val lastUpdateTime: Long, // since we provide converters, maybe this should still be a Date
    val cocktailName: String? = null,
    val isAlcoholic: Boolean = false,
    val type: String? = null,
    val method: String? = null,
    val glass: String? = null,
    val ingredients: Map<String, String> = emptyMap(),
    val instructions: String? = null,
    @ColumnInfo(defaultValue = "''")
    val instructionsIt: String = "",
    val imageUrl: String? = null
)

fun CreateCocktailEntity.toModel() = CreateCocktailModel(
    id = uniqueId,
    currentStep = currentStep,
    createdTime = Date(createdTime),
    lastUpdateTime = Date(lastUpdateTime),
    cocktailName = cocktailName,
    isAlcoholic = isAlcoholic,
    type = type,
    method = method,
    glass = glass,
    ingredients = ingredients,
    instructions = instructions,
    instructionsIt = instructionsIt,
    imageUrl = imageUrl
)