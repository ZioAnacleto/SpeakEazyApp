package com.zioanacleto.speakeazy.ui.presentation.create.domain.model

import com.zioanacleto.speakeazy.database.entities.CreateCocktailEntity
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import java.util.Date

data class CreateCocktailModel(
    val id: Int? = null,
    val currentStep: CreateWizardStepData,
    val createdTime: Date,
    val lastUpdateTime: Date,
    val cocktailName: String? = null,
    val isAlcoholic: Boolean = false,
    val type: String? = null,
    val method: String? = null,
    val glass: String? = null,
    val ingredients: Map<String, String> = emptyMap(),
    val instructions: String? = null,
    val instructionsIt: String = "",
    val imageUrl: String? = null,
    val userId: String = "1",
    val username: String = ""
)

fun CreateCocktailModel.toEntity() = CreateCocktailEntity(
    uniqueId = id,
    currentStep = currentStep,
    createdTime = createdTime.time,
    lastUpdateTime = lastUpdateTime.time,
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