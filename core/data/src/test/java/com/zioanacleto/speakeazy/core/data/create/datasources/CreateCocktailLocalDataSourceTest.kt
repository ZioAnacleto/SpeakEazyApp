package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.CreateCocktailDao
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class CreateCocktailLocalDataSourceTest {

    private lateinit var createCocktailDao: CreateCocktailDao

    @Before
    fun setUp() {
        createCocktailDao = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getLocalCreateCocktail - should return correct data`() = runBlocking {
        // given
        every { createCocktailDao.getAll() } returns listOf()

        // when
        val sut = createSut()
        val response = sut.getLocalCreateCocktail()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.isEmpty()
        )
    }

    @Test
    fun `getLocalCreateCocktail - when catch Exception should return Error`() = runBlocking {
        // given
        every { createCocktailDao.getAll() } throws Exception()

        // when
        val sut = createSut()
        val response = sut.getLocalCreateCocktail()

        // then
        assert(response is Resource.Error)
    }

    @Test
    fun `saveLocalCreateCocktail - should return correct data`() = runBlocking {
        // given
        every { createCocktailDao.upsert(any()) } returns 1
        val input = CreateCocktailModel(
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

        // when
        val sut = createSut()
        val response = sut.saveLocalCreateCocktail(input)

        // then
        assert(response == 1)
    }

    @Test
    fun `deleteLocalCreateCocktail - should return true`() = runBlocking {
        // given
        every { createCocktailDao.delete(any()) } just runs
        val uniqueId = 1

        // when
        val sut = createSut()
        val response = sut.deleteLocalCreateCocktail(uniqueId)

        // then
        assert(response)
    }

    @Test
    fun `deleteLocalCreateCocktail - on error should return false`() = runBlocking {
        // given
        every { createCocktailDao.delete(any()) } throws Exception()
        val uniqueId = 1

        // when
        val sut = createSut()
        val response = sut.deleteLocalCreateCocktail(uniqueId)

        // then
        assert(!response)
    }

    private fun createSut() = CreateCocktailLocalDataSource(createCocktailDao)

}