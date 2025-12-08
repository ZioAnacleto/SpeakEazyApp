package com.zioanacleto.speakeazy.ui.presentation.search.data.dto

import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDTO(
    @SerialName("cocktails") val cocktails: List<MainSpeakEazyBEResponseDTO>
)
