package com.zioanacleto.speakeazy.core.network.builders

object SpeakEazyBEUrlBuilder {
    private const val BASE_URL = "https://speakeazybackend-production.up.railway.app"
    private const val COCKTAILS_URI = "cocktails"
    private const val INGREDIENTS_URI = "ingredients"
    private const val HOME_URI = "home"
    private const val SEARCH_URI = "search"
    private const val TAGS_URI = "tags"
    private const val FILTER_URI = "filter"
    private const val INGREDIENT_QUERY_PARAM = "ingredient="
    private const val FILTER_QUERY_PARAM = "filter="
    private const val ADD_URI = "add"

    fun buildUrl(endpoint: Endpoint): String = "$BASE_URL/${endpoint.url}"

    sealed class Endpoint {
        abstract val url: String

        data object Cocktails : Endpoint() {
            override val url: String = COCKTAILS_URI
        }

        data class SingleCocktail(
            val id: String
        ) : Endpoint() {
            override val url: String = "$COCKTAILS_URI/$id"
        }

        data object CreateCocktail : Endpoint() {
            override val url: String = "$COCKTAILS_URI/$ADD_URI"
        }

        data object Ingredients : Endpoint() {
            override val url: String = INGREDIENTS_URI
        }

        data class SingleIngredient(
            val id: String
        ) : Endpoint() {
            override val url: String = "$INGREDIENTS_URI/$id"
        }

        data object Home : Endpoint() {
            override val url: String = HOME_URI
        }

        data object Search : Endpoint() {
            override val url: String = SEARCH_URI
        }

        data object Tags : Endpoint() {
            override val url: String = TAGS_URI
        }

        data class Filter(
            val ingredients: List<String>? = null,
            val tags: List<String>? = null
        ) : Endpoint() {
            override val url: String
                get() = run {
                    val builder = StringBuilder("$SEARCH_URI/$FILTER_URI?")
                    ingredients?.forEachIndexed { index, ingredient ->
                        if (index != 0) builder.append("&")
                        builder.append("$INGREDIENT_QUERY_PARAM$ingredient")
                    }
                    tags?.forEachIndexed { index, tag ->
                        if (index != 0) builder.append("&")
                        builder.append("$FILTER_QUERY_PARAM$tag")
                    }
                    builder.toString()
                }
        }
    }
}