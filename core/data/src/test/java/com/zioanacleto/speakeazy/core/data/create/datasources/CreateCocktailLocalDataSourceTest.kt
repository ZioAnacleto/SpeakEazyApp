package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.CreateCocktailDao
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

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

    private fun createSut() = CreateCocktailLocalDataSource(createCocktailDao)

}