package com.zioanacleto.speakeazy.core.data.main.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainListResponseDTO(
    @SerialName("drinks") val drinks: List<SimpleDrinkDTO?>?
) {

    @Serializable
    data class SimpleDrinkDTO(
        @SerialName("strDrink") val strDrink: String?,
        @SerialName("strDrinkThumb") val strDrinkThumb: String?,
        @SerialName("idDrink") val idDrink: String?
    )
}