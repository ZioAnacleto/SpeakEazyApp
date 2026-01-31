package com.zioanacleto.speakeazy.core.domain.main.model

data class HomeModel(
    val sections: List<HomeSectionModel>,
    val banner: BannerModel? = null
)

data class HomeSectionModel(
    val id: String,
    val name: String,
    val cocktails: List<DrinkModel>
)

data class BannerModel(
    val name: String,
    val position: String,
    val cta: String?,
    val cocktail: DrinkModel
)
