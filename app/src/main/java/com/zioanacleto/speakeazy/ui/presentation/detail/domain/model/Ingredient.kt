package com.zioanacleto.speakeazy.ui.presentation.detail.domain.model

data class IngredientsModel(
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val id: String,
    val name: String,
    val imageUrl: String = ""
)
