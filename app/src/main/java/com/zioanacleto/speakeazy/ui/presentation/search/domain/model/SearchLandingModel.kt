package com.zioanacleto.speakeazy.ui.presentation.search.domain.model

import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.Ingredient

data class SearchLandingModel(
    val ingredients: List<Ingredient>,
    val tags: List<TagModel>
)

data class TagsModel(
    val tags: List<TagModel>
)

data class TagModel(
    val id: String,
    val name: String
)
