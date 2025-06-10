package com.zioanacleto.speakeazy.ui.presentation.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagsResponseDTO(
    @SerialName("tags") val tags: List<TagDTO>
)

@Serializable
data class TagDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)
