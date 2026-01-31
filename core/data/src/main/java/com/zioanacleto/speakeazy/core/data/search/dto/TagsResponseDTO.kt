package com.zioanacleto.speakeazy.core.data.search.dto

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
