package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoritesNetworkDataSourceTest {

    private lateinit var apiClient: ApiClientImpl
    private lateinit var dataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel>

    @Before
    fun setUp() {
        dataMapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getCocktails_whenResponseOK_returnSuccess() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = """{"cocktails": []}"""
        )

        every {
            dataMapper.mapInto(any())
        } returns FavoritesModel(listOf())

        val sut = createSut()

        // when
        val result = sut.getCocktails()

        // then
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.favorites.isEmpty())
    }

    @Test
    fun test_getCocktails_whenResponseKO_returnError() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.InternalServerError,
            response = ""
        )

        every {
            dataMapper.mapInto(any())
        } returns FavoritesModel(listOf())

        val sut = createSut()

        // when
        val result = sut.getCocktails()

        // then
        assertTrue(result is Resource.Error)
    }

    private fun createSut() = FavoritesNetworkDataSource(
        apiClient,
        dataMapper
    )

    private fun createApiClientWithResponse(
        status: HttpStatusCode,
        response: String
    ) =
        ApiClientImpl(
            MockEngine { _ ->
                respond(
                    status = status,
                    content = response,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
}