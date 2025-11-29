package com.zioanacleto.speakeazy.ui.presentation.main.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class MainSpeakEazyBEListResponseDTO(
    @SerialName("cocktails") val cocktails: List<MainSpeakEazyBEResponseDTO>
)

@Serializable
data class MainSpeakEazyBEResponseDTO(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
    @SerialName("category") val category: String?,
    @SerialName("instructions") val instructions: String?,
    @SerialName("instructionsIt") val instructionsIt: String?,
    @SerialName("glass") val glass: String?,
    @SerialName("isAlcoholic") val isAlcoholic: Boolean?,
    @SerialName("imageLink") val imageLink: String?,
    @SerialName("type") val type: String?,
    @SerialName("method") val method: String?,
    @SerialName("ingredients") val ingredients: MainSpeakEazyBEIngredientsListDTO?,
    @SerialName("visualizations") val visualizations: Long?,
    @SerialName("username") val username: String?,
    @SerialName("userId") val userId: String?
)

@Serializable
data class MainSpeakEazyBEIngredientsListDTO(
    @SerialName("ingredients") val ingredients: List<MainSpeakEazyBEIngredientDTO>?
)

@Serializable
data class MainSpeakEazyBEIngredientDTO(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String? = "",
    @SerialName("imageUrl") val imageUrl: String? = "",
    @SerialName("quantityCl") val quantityCl: String?,
    @SerialName("quantityOz") val quantityOz: String?,
    @SerialName("quantitySpecial") val quantitySpecial: String? = null
)