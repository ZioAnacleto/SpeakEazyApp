package com.zioanacleto.speakeazy.core.domain.search.model

import com.zioanacleto.speakeazy.core.domain.detail.model.Ingredient

data class SearchLandingModel(
    val ingredients: List<Ingredient>,
    val tags: List<TagModel>,
    val lastQueries: List<QueryModel>
)

data class TagsModel(
    val tags: List<TagModel>
)

data class TagModel(
    val id: String,
    val name: String
)

data class QueryModel(
    val query: String,
    val lastUsed: Long
)
