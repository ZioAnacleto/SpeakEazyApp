package com.zioanacleto.speakeazy.core.data.search.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestDTO(
    @SerialName("query") val query: String
)
