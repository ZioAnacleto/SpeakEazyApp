package com.zioanacleto.speakeazy.core.network.builders

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

//TODO: how to test correctly Filter?
@RunWith(Parameterized::class)
class SpeakEazyBEUrlBuilderTest(
    private val endpoint: SpeakEazyBEUrlBuilder.Endpoint,
    private val expectedUrl: String
) {

    @Test
    fun `buildUrl - Endpoint should return correct endpoint`() {
        // when
        val url = SpeakEazyBEUrlBuilder.buildUrl(endpoint)
        val folders = url.split("/")

        // then
        assert(folders.last() == expectedUrl)
    }

    companion object {
        private const val STATIC_ID = "25"
        @JvmStatic
        @Parameterized.Parameters(name = "{0} should return {1}")
        fun provideData() = listOf(
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.Cocktails, "cocktails"),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.SingleCocktail(STATIC_ID), STATIC_ID),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.CreateCocktail, "add"),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.Ingredients, "ingredients"),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.SingleIngredient(STATIC_ID), STATIC_ID),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.Home, "home"),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.Search, "search"),
            arrayOf(SpeakEazyBEUrlBuilder.Endpoint.Tags, "tags")
        )
    }
}