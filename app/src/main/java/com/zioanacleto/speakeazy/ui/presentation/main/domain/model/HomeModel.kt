package com.zioanacleto.speakeazy.ui.presentation.main.domain.model

data class HomeModel(
    val sections: List<HomeSectionModel>
)

data class HomeSectionModel(
    val id: String,
    val name: String,
    val cocktails: List<DrinkModel>
)
