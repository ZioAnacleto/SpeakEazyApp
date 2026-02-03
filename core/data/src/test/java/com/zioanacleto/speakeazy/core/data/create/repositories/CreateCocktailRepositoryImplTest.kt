package com.zioanacleto.speakeazy.core.data.create.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailDataSource
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailUploadDataSource
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class CreateCocktailRepositoryImplTest {

    private lateinit var localDataSource: CreateCocktailDataSource
    private lateinit var networkDataSource: CreateCocktailUploadDataSource
    private lateinit var ingredientDataSource: IngredientDataSource
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        localDataSource = mockk(relaxed = true)
        networkDataSource = mockk(relaxed = true)
        ingredientDataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCreateCocktail - should return correct data`() = runTest {
        // given
        coEvery { localDataSource.getLocalCreateCocktail() } returns Resource.Success(
            listOf(
                mockCreateCocktailModel()
            )
        )

        // when
        val sut = createSut()
        val response = sut.getCreateCocktail().first()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.isNotEmpty()
        )
    }

    @Test
    fun `getCreateCocktail - should return Error`() = runTest {
        // given
        coEvery { localDataSource.getLocalCreateCocktail() } returns Resource.Error(
            Exception()
        )

        // when
        val sut = createSut()
        val response = sut.getCreateCocktail().first()

        // then
        assert(response is Resource.Error)
    }

    @Test
    fun `saveCreateCocktail - should return correct status code`() = runTest {
        // given
        coEvery { localDataSource.saveLocalCreateCocktail(any()) } returns 1
        val input = mockCreateCocktailModel()

        // when
        val sut = createSut()
        val response = sut.saveCreateCocktail(input)

        // then
        assert(response == 1)
    }

    @Test
    fun `deleteCreateCocktail - should return true`() = runTest {
        // given
        val uniqueId = 1
        coEvery { localDataSource.deleteLocalCreateCocktail(uniqueId) } returns true

        // when
        val sut = createSut()
        val response = sut.deleteCreateCocktail(uniqueId)

        // then
        assert(response)
    }

    @Test
    fun `deleteCreateCocktail - should return false`() = runTest {
        // given
        val uniqueId = 1
        coEvery { localDataSource.deleteLocalCreateCocktail(uniqueId) } returns false

        // when
        val sut = createSut()
        val response = sut.deleteCreateCocktail(uniqueId)

        // then
        assert(!response)
    }

    @Test
    fun `getIngredients - should return correct data`() = runTest {
        // given
        coEvery { ingredientDataSource.getIngredientsList() } returns Resource.Success(
            IngredientsModel(
                listOf()
            )
        )

        // when
        val sut = createSut()
        val response = sut.getIngredients().first()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.ingredients.isEmpty()
        )
    }

    @Test
    fun `getIngredients - should return Error`() = runTest {
        // given
        coEvery { ingredientDataSource.getIngredientsList() } returns Resource.Error(
            Exception()
        )

        // when
        val sut = createSut()
        val response = sut.getIngredients().first()

        // then
        assertAllTrue(
            response is Resource.Error
        )
    }

    @Test
    fun `uploadCocktail - should return true`() = runTest {
        // given
        coEvery { networkDataSource.uploadCocktail(any()) } returns Resource.Success(
            "test"
        )

        // when
        val sut = createSut()
        val response = sut.uploadCocktail(mockCreateCocktailModel())

        // then
        assert(response)
    }

    @Test
    fun `uploadCocktail - should return false`() = runTest {
        // given
        coEvery { networkDataSource.uploadCocktail(any()) } returns Resource.Error(
            Exception()
        )

        // when
        val sut = createSut()
        val response = sut.uploadCocktail(mockCreateCocktailModel())

        // then
        assert(!response)
    }

    private fun createSut() = CreateCocktailRepositoryImpl(
        localDataSource,
        networkDataSource,
        ingredientDataSource,
        dispatcherProvider
    )

    private fun mockCreateCocktailModel() = CreateCocktailModel(
        currentStep = 0,
        createdTime = Date(),
        lastUpdateTime = Date(),
        cocktailName = "testCocktailName",
        isAlcoholic = true,
        type = "testType",
        method = "testMethod",
        glass = "testGlass",
        ingredients = mapOf("testIngredient" to "1 cl"),
        instructions = "testInstructions",
        instructionsIt = "testInstructionsIt",
        imageUrl = "testImageUrl",
        userId = "testUserId",
        username = "testUsername"
    )

}