package com.zioanacleto.speakeazy.core.domain.search.model

data class SearchModel(
    val results: List<SearchItem> = listOf()
)

sealed interface SearchItem {
    data class Cocktail(
        val id: String,
        val name: String,
        val imageUrl: String
    ): SearchItem

    data class Ingredient(
        val id: String,
        val name: String
    )
}
