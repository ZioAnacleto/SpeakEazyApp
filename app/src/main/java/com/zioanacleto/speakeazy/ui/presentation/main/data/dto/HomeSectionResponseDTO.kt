package com.zioanacleto.speakeazy.ui.presentation.main.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeSectionResponseDTO(
    @SerialName("sections") val sections: List<HomeSectionDTO>
)

@Serializable
data class HomeSectionDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("cocktails") val cocktails: List<MainSpeakEazyBEResponseDTO>
)