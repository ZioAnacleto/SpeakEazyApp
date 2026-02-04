package com.zioanacleto.speakeazy.core.network.builders

import org.junit.Assert.assertEquals
import org.junit.Test

class TheCocktailDBUrlBuilderTest {

    @Test
    fun `buildUrl - Random will return correct endpoint`() {
        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(TheCocktailDBUrlBuilder.Endpoint.Random)
        val expectedEndpoint = "random.php"
        val folders = url.split("/")

        // then
        assertEquals(expectedEndpoint, folders.last())
    }

    @Test
    fun `buildUrl - Search with NAME query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Search(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Search.QueryMode.NAME
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "s=$parameter")
    }

    @Test
    fun `buildUrl - Search with FIRST_LETTER query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Search(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Search.QueryMode.FIRST_LETTER
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "f=$parameter")
    }

    @Test
    fun `buildUrl - Search with INGREDIENT query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Search(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Search.QueryMode.INGREDIENT
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "i=$parameter")
    }

    @Test
    fun `buildUrl - Lookup with ID query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Lookup(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Lookup.QueryMode.ID
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "i=$parameter")
    }

    @Test
    fun `buildUrl - Lookup with IID query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Lookup(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Lookup.QueryMode.IID
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "iid=$parameter")
    }

    @Test
    fun `buildUrl - Filter with ALCOHOLIC query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Filter(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Filter.QueryMode.ALCOHOLIC
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "a=$parameter")
    }

    @Test
    fun `buildUrl - Filter with INGREDIENTS query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Filter(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Filter.QueryMode.INGREDIENTS
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "i=$parameter")
    }

    @Test
    fun `buildUrl - Filter with CATEGORY query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Filter(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Filter.QueryMode.CATEGORY
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "c=$parameter")
    }

    @Test
    fun `buildUrl - Filter with GLASS query mode should return correct endpoint`() {
        // given
        val parameter = "testParameter"

        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.Filter(
                parameter,
                TheCocktailDBUrlBuilder.Endpoint.Filter.QueryMode.GLASS
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "g=$parameter")
    }

    @Test
    fun `buildUrl - List with ALCOHOLIC query mode should return correct endpoint`() {
        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.List(
                TheCocktailDBUrlBuilder.Endpoint.List.QueryMode.ALCOHOLIC
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "a=list")
    }

    @Test
    fun `buildUrl - List with INGREDIENTS query mode should return correct endpoint`() {
        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.List(
                TheCocktailDBUrlBuilder.Endpoint.List.QueryMode.INGREDIENTS
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "i=list")
    }

    @Test
    fun `buildUrl - List with CATEGORY query mode should return correct endpoint`() {
        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.List(
                TheCocktailDBUrlBuilder.Endpoint.List.QueryMode.CATEGORY
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "c=list")
    }

    @Test
    fun `buildUrl - List with GLASS query mode should return correct endpoint`() {
        // when
        val url = TheCocktailDBUrlBuilder.buildUrl(
            TheCocktailDBUrlBuilder.Endpoint.List(
                TheCocktailDBUrlBuilder.Endpoint.List.QueryMode.GLASS
            )
        )
        val lastUri = url.findLastUri()

        // then
        assertEquals(lastUri, "g=list")
    }

    private fun String.findLastUri() = split("/").last().split("?").last()
}