package com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.createApiClientWithResponse
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IngredientNetworkDataSourceTest {

    private lateinit var apiClient: ApiClientImpl
    private lateinit var dataMapper: DataMapper<IngredientsListDTO, IngredientsModel>

    @Before
    fun setUp() {
        dataMapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getIngredientsList_whenResponseOK_returnSuccess() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = "{\"ingredients\": []}"
        )

        every {
            dataMapper.mapInto(any())
        } returns IngredientsModel(listOf())

        // when
        val sut = createSut()
        val result = sut.getIngredientsList()

        // then
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.ingredients.isEmpty())
    }

    @Test
    fun test_getIngredientsList_whenResponseKO_returnError() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.BadRequest,
            response = ""
        )

        every {
            dataMapper.mapInto(any())
        } returns IngredientsModel(listOf())

        // when
        val sut = createSut()
        val result = sut.getIngredientsList()

        // then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun test_getIngredientById_whenResponseOK_returnSuccess() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = "{\"ingredients\": []}"
        )

        every {
            dataMapper.mapInto(any())
        } returns IngredientsModel(listOf())

        // when
        val sut = createSut()
        val result = sut.getIngredientById("1")

        // then
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.ingredients.isEmpty())
    }

    @Test
    fun test_getIngredientById_whenResponseKO_returnError() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.BadRequest,
            response = ""
        )

        every {
            dataMapper.mapInto(any())
        } returns IngredientsModel(listOf())

        // when
        val sut = createSut()
        val result = sut.getIngredientById("1")

        // then
        assertTrue(result is Resource.Error)
    }

    private fun createSut() = IngredientNetworkDataSource(
        apiClient,
        dataMapper
    )
}