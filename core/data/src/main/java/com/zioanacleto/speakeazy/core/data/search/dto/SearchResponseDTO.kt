package com.zioanacleto.speakeazy.core.data.search.dto

import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDTO(
    @SerialName("cocktails") val cocktails: List<MainSpeakEazyBEResponseDTO>
)
