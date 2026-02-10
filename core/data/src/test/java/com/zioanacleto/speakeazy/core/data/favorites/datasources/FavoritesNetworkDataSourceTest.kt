package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.createApiClientWithResponse
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoritesNetworkDataSourceTest {

    private lateinit var apiClient: ApiClientImpl
    private lateinit var dataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel>
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        performanceTracesManager = mockk(relaxed = true)
        every { performanceTracesManager.startTrace(any(), any()) } just runs
        every { performanceTracesManager.stopTrace(any(), any()) } just runs
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
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.favorites.isEmpty()
        )
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
        dataMapper,
        performanceTracesManager
    )
}