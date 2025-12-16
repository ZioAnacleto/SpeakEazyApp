package com.zioanacleto.speakeazy.ui.presentation.create.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.createApiClientWithResponse
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import com.zioanacleto.speakeazy.ui.presentation.create.data.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.create.data.dto.TagsRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEIngredientsListDTO
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class CreateCocktailNetworkUploadDataSourceTest {

    private lateinit var apiClient: ApiClientImpl
    private lateinit var requestDataMapper: DataMapper<CreateCocktailModel, CreateCocktailRequestDTO>

    @Before
    fun setUp() {
        requestDataMapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_uploadCocktail_whenResponseIsOK_returnSuccess() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = 1.toString()
        )
        every { requestDataMapper.mapInto(any()) } returns CreateCocktailRequestDTO(
            id = "1000",
            name = "testName",
            category = "SpeakEazy Original",
            instructions = listOf(),
            instructionsIt = listOf(),
            glass = "testGlass",
            isAlcoholic = true,
            imageLink = "testImageUrl",
            type = "testType",
            method = "testMethod",
            ingredients = MainSpeakEazyBEIngredientsListDTO(ingredients = listOf()),
            visualizations = 1,
            tags = TagsRequestDTO(listOf()),
            userId = "testUserId",
            username = "testUsername"
        )

        // when
        val sut = createSut()
        val response = sut.uploadCocktail(
            CreateCocktailModel(
                id = 1000,
                currentStep = CreateWizardStepData.First,
                createdTime = Date(),
                lastUpdateTime = Date(),
            )
        )

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data == "1"
        )
    }

    @Test
    fun test_uploadCocktail_whenResponseIsKO_returnError() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.BadRequest,
            response = ""
        )
        every { requestDataMapper.mapInto(any()) } returns CreateCocktailRequestDTO(
            id = "1000",
            name = "testName",
            category = "SpeakEazy Original",
            instructions = listOf(),
            instructionsIt = listOf(),
            glass = "testGlass",
            isAlcoholic = true,
            imageLink = "testImageUrl",
            type = "testType",
            method = "testMethod",
            ingredients = MainSpeakEazyBEIngredientsListDTO(ingredients = listOf()),
            visualizations = 1,
            tags = TagsRequestDTO(listOf()),
            userId = "testUserId",
            username = "testUsername"
        )

        // when
        val sut = createSut()
        val response = sut.uploadCocktail(
            CreateCocktailModel(
                id = 1000,
                currentStep = CreateWizardStepData.First,
                createdTime = Date(),
                lastUpdateTime = Date(),
            )
        )

        // then
        assert(response is Resource.Error)
    }

    private fun createSut() = CreateCocktailNetworkUploadDataSource(apiClient, requestDataMapper)
}