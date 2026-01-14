package com.zioanacleto.speakeazy.core.domain.create.model

import java.util.Date

data class CreateCocktailModel(
    val id: Int? = null,
    val currentStep: Int,
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