package com.zioanacleto.speakeazy.core.data.create.dto

import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEIngredientsListDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCocktailRequestDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("instructions") val instructions: String,
    @SerialName("instructionsIt") val instructionsIt: String,
    @SerialName("glass") val glass: String,
    @SerialName("isAlcoholic") val isAlcoholic: Boolean,
    @SerialName("imageLink") val imageLink: String,
    @SerialName("type") val type: String,
    @SerialName("method") val method: String,
    @SerialName("ingredients") val ingredients: MainSpeakEazyBEIngredientsListDTO,
    @SerialName("visualizations") val visualizations: Long,
    @SerialName("tags") val tags: TagsRequestDTO,
    @SerialName("userId") val userId: String,
    @SerialName("username") val username: String
)

@Serializable
data class TagsRequestDTO(
    @SerialName("tags") val tags: List<String>
)