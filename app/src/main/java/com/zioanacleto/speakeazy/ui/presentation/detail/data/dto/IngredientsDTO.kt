package com.zioanacleto.speakeazy.ui.presentation.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientsListDTO(
    @SerialName("ingredients") val ingredients: List<IngredientDTO>?
)

@Serializable
data class IngredientDTO(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
    @SerialName("imageUrl") val imageUrl: String?
)