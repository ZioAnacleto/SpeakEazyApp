package com.zioanacleto.speakeazy.core.network.builders

object TheCocktailDBUrlBuilder {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1"
    private const val API_KEY = "1"

    fun buildUrl(endpoint: Endpoint): String =
        "$BASE_URL/$API_KEY/${endpoint.buildRelativeUri()}"

    sealed class Endpoint(
        val uri: String
    ) {
        abstract fun buildRelativeUri(): String

        data object Random : Endpoint("random.php") {
            override fun buildRelativeUri(): String = this.uri
        }

        data class Search(
            private val parameter: String,
            private val queryMode: QueryMode,
        ) : Endpoint("search.php") {
            override fun buildRelativeUri() = "$uri?${queryMode.param}=$parameter"

            enum class QueryMode(val param: String) {
                NAME(QUERY_PARAM_NAME),
                FIRST_LETTER(QUERY_PARAM_FIRST_LETTER),
                INGREDIENT(QUERY_PARAM_INGREDIENT)
            }

            companion object {
                private const val QUERY_PARAM_NAME = "s"
                private const val QUERY_PARAM_FIRST_LETTER = "f"
                private const val QUERY_PARAM_INGREDIENT = "i"
            }
        }

        data class Lookup(
            private val parameter: String,
            private val queryMode: QueryMode
        ) : Endpoint("lookup.php") {
            override fun buildRelativeUri() = "$uri?${queryMode.param}=$parameter"

            enum class QueryMode(val param: String) {
                ID(QUERY_PARAM_ID),
                IID(QUERY_PARAM_IID)
            }

            companion object {
                private const val QUERY_PARAM_ID = "i"
                private const val QUERY_PARAM_IID = "iid"
            }
        }

        data class Filter(
            private val parameter: String,
            private val queryMode: QueryMode
        ): Endpoint("filter.php") {

            override fun buildRelativeUri() = "$uri?${queryMode.param}=$parameter"

            enum class QueryMode(val param: String) {
                ALCOHOLIC(QUERY_PARAM_ALCOHOLIC),
                INGREDIENTS(QUERY_PARAM_INGREDIENTS),
                CATEGORY(QUERY_PARAM_CATEGORY),
                GLASS(QUERY_PARAM_GLASS)
            }

            companion object {
                private const val QUERY_PARAM_ALCOHOLIC = "a"
                private const val QUERY_PARAM_INGREDIENTS = "i"
                private const val QUERY_PARAM_CATEGORY = "c"
                private const val QUERY_PARAM_GLASS = "g"
            }
        }

        data class List(
            private val queryMode: QueryMode
        ): Endpoint("list.php") {
            override fun buildRelativeUri() = "$uri?${queryMode.param}=list"

            enum class QueryMode(val param: String) {
                ALCOHOLIC(QUERY_PARAM_ALCOHOLIC),
                INGREDIENTS(QUERY_PARAM_INGREDIENTS),
                CATEGORY(QUERY_PARAM_CATEGORY),
                GLASS(QUERY_PARAM_GLASS)
            }

            companion object {
                private const val QUERY_PARAM_ALCOHOLIC = "a"
                private const val QUERY_PARAM_INGREDIENTS = "i"
                private const val QUERY_PARAM_CATEGORY = "c"
                private const val QUERY_PARAM_GLASS = "g"
            }
        }
    }
}