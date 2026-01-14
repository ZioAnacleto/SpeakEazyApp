package com.zioanacleto.speakeazy.core.data.main.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeSectionResponseDTO(
    @SerialName("sections") val sections: List<HomeSectionDTO>,
    @SerialName("banner") val banner: BannerDTO? = null
)

@Serializable
data class HomeSectionDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("cocktails") val cocktails: List<MainSpeakEazyBEResponseDTO>
)

@Serializable
data class BannerDTO(
    @SerialName("position") val position: String,
    @SerialName("name") val name: String,
    @SerialName("cta") val cta: String? = null,
    @SerialName("cocktailInfo") val cocktailInfo: MainSpeakEazyBEResponseDTO,
)