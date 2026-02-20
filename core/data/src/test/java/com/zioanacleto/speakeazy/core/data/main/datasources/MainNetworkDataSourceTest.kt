package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.createApiClientWithResponse
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainNetworkDataSourceTest {

    private lateinit var apiClient: ApiClientImpl
    private lateinit var dataMapper: DataMapper<MainSpeakEazyBEResponseDTO, MainModel>
    private lateinit var listDataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        dataMapper = mockk(relaxed = true)
        listDataMapper = mockk(relaxed = true)
        performanceTracesManager = mockk(relaxed = true) {
            every { startTrace(any(), any()) } just runs
            every { stopTrace(any(), any()) } just runs
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getMainList - should return correct data when 200`() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = """{"cocktails": []}"""
        )
        every { listDataMapper.mapInto(any()) } returns MainModel(drinks = listOf())

        // when
        val sut = createSut()
        val response = sut.getMainList()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.drinks.isEmpty()
        )
    }

    @Test
    fun `getMainList - should return Error when catching Exception`() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.NotFound,
            response = ""
        )
        every { listDataMapper.mapInto(any()) } returns MainModel(drinks = listOf())

        // when
        val sut = createSut()
        val response = sut.getMainList()

        // then
        assertAllTrue(
            response is Resource.Error
        )
    }

    @Test
    fun `getMainById - should return Success when 200`() = runBlocking {
        // given
        val id = "1"
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = """{
            "id": "8",
            "name": "testName",
            "category": "testCategory",
            "instructions": [
                {
                    "type": "testInstructionType",
                    "instruction": "testInstruction"
                }
            ],
            "instructionsIt": [
                {
                    "type": "testInstructionType",
                    "instruction": "testInstruction"
                }
            ],
            "glass": "testGlass",
            "isAlcoholic": true,
            "imageLink": "testImageLink",
            "videoLink": "testVideoLink",
            "type": "testType",
            "method": "testMethod",
            "ingredients": {
                "ingredients": []
            },
            "visualizations": 1,
            "tags": {},
            "userId": "1",
            "username": ""
        }"""
        )
        every { dataMapper.mapInto(any()) } returns MainModel(drinks = listOf())

        // when
        val sut = createSut()
        val response = sut.getMainById(id)

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.drinks.isEmpty()
        )
    }

    @Test
    fun `getMainById - should return Error when catching Exception`() = runBlocking {
        // given
        val id = "1"
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.BadGateway,
            response = ""
        )
        every { dataMapper.mapInto(any()) } returns MainModel(drinks = listOf())

        // when
        val sut = createSut()
        val response = sut.getMainById(id)

        // then
        assertAllTrue(
            response is Resource.Error
        )
    }

    @Test
    fun `updateVisualizations - correct method on ApiClient should be called`() = runTest {
        // given
        val cocktailId = "1"
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = "1"
        )

        // when
        val sut = createSut()
        sut.updateVisualizations(cocktailId)
    }

    private fun createSut() =
        MainNetworkDataSource(apiClient, dataMapper, listDataMapper, performanceTracesManager)

}