package com.zioanacleto.speakeazy.data.builders

object TheCocktailDBUrlBuilder {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1"
    private const val API_KEY = "1"

    fun buildUrl(endpoint: Endpoint): String =
        "$BASE_URL/$API_KEY/${endpoint.buildQuery()}"

    sealed class Endpoint(
        val url: String
    ) {
        abstract fun buildQuery(): String

        data object Random : Endpoint("random.php") {
            override fun buildQuery(): String = this.url
        }

        data class Search(
            private val parameter: String,
            private val queryMode: QueryMode,
        ) : Endpoint("search.php") {
            override fun buildQuery() = "$url?${queryMode.param}=$parameter"

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
            override fun buildQuery() = "$url?${queryMode.param}=$parameter"

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

            override fun buildQuery() = "$url?${queryMode.param}=$parameter"

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

                const val ALCOHOLIC = "Alcoholic"
                const val NON_ALCOHOLIC = "Non_Alcoholic"
            }
        }

        data class List(
            private val queryMode: QueryMode
        ): Endpoint("list.php") {
            override fun buildQuery() = "$url?${queryMode.param}=list"

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