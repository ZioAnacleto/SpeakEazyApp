package com.zioanacleto.speakeazy.ui.presentation.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestDTO(
    @SerialName("query") val query: String
)
