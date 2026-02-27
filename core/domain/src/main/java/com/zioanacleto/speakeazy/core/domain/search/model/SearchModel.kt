package com.zioanacleto.speakeazy.core.domain.search.model

data class SearchModel(
    val results: List<SearchItem> = listOf()
)

sealed interface SearchItem {
    val id: String
    val name: String
    val imageUrl: String

    data class Cocktail(
        override val id: String,
        override val name: String,
        override val imageUrl: String,
        val category: String,
        val favorite: Boolean,
        val username: String? = null
    ) : SearchItem

    // todo: add ingredients as result when BE allows it
    /*data class Ingredient(
        override val id: String,
        override val name: String,
        override val imageUrl: String
    ) : SearchItem*/
}
