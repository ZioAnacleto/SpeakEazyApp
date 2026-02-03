package com.zioanacleto.speakeazy.core.data.detail.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.IngredientDao
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class IngredientLocalDataSourceTest {
    private lateinit var ingredientDao: IngredientDao

    @Before
    fun setUp() {
        ingredientDao = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getIngredientsList - returns Success when OK`() = runBlocking{
        // given
        every { ingredientDao.getAll() } returns listOf()

        // when
        val sut = createSut()
        val result = sut.getIngredientsList()

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.ingredients.isEmpty()
        )
    }

    @Test
    fun `getIngredientsList - returns Error when KO`() = runBlocking{
        // given
        every { ingredientDao.getAll() } throws Exception()

        // when
        val sut = createSut()
        val result = sut.getIngredientsList()

        // then
        assertAllTrue(
            result is Resource.Error
        )
    }

    @Test
    fun `getIngredientById - returns Success when OK`() = runBlocking{
        // given
        val id = "1"
        every { ingredientDao.loadAllByIds(any()) } returns listOf()

        // when
        val sut = createSut()
        val result = sut.getIngredientById(id)

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.ingredients.isEmpty()
        )
    }

    @Test
    fun `getIngredientById - returns Error when KO`() = runBlocking{
        // given
        val id = "1"
        every { ingredientDao.loadAllByIds(any()) } throws Exception()

        // when
        val sut = createSut()
        val result = sut.getIngredientById(id)

        // then
        assertAllTrue(
            result is Resource.Error
        )
    }

    private fun createSut() = IngredientLocalDataSource(ingredientDao)
}