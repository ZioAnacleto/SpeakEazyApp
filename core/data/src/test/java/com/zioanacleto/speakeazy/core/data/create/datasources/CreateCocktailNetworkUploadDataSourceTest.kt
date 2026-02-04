package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.create.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.core.data.create.dto.TagsRequestDTO
import com.zioanacleto.speakeazy.core.data.createApiClientWithResponse
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEIngredientsListDTO
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
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

    private lateinit var apiClientImpl: ApiClientImpl
    private lateinit var dataMapper: DataMapper<CreateCocktailModel, CreateCocktailRequestDTO>

    @Before
    fun setUp() {
        dataMapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `uploadCocktail - when response is OK should return Success`() = runBlocking {
        // given
        apiClientImpl = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = "1"
        )
        every { dataMapper.mapInto(any()) } returns mockCreateCocktailRequestDTO()

        // when
        val sut = createSut()
        val result = sut.uploadCocktail(
            CreateCocktailModel(
                id = 1,
                currentStep = 1,
                createdTime = Date(),
                lastUpdateTime = Date()
            )
        )

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data == "1"
        )
    }

    private fun createSut() = CreateCocktailNetworkUploadDataSource(apiClientImpl, dataMapper)

    private fun mockCreateCocktailRequestDTO() = CreateCocktailRequestDTO(
        id = "1",
        name = "testName",
        category = "testCategory",
        glass = "testGlass",
        instructions = "testInstructions",
        instructionsIt = "testInstructionsIt",
        isAlcoholic = true,
        imageLink = "testImageLink",
        type = "testType",
        method = "testMethod",
        ingredients = MainSpeakEazyBEIngredientsListDTO(ingredients = null),
        visualizations = 1,
        tags = TagsRequestDTO(tags = listOf()),
        userId = "1",
        username = "testUsername"
    )

}