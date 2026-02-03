package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoritesLocalDataSourceTest {
    private lateinit var favoritesDao: FavoritesDao

    @Before
    fun setUp() {
        favoritesDao = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCocktails - returns Success when OK`() = runBlocking {
        // given
        every { favoritesDao.getAll() } returns listOf()

        // when
        val sut = createSut()
        val result = sut.getCocktails()

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.favorites.isEmpty()
        )
    }

    @Test
    fun `getCocktails - returns Error when KO`() = runBlocking {
        // given
        every { favoritesDao.getAll() } throws Exception()

        // when
        val sut = createSut()
        val result = sut.getCocktails()

        // then
        assertAllTrue(
            result is Resource.Error
        )
    }

    private fun createSut() = FavoritesLocalDataSource(favoritesDao)

}