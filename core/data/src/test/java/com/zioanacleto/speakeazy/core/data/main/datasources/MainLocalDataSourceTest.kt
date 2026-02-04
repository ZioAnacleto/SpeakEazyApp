package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.core.database.entities.FavoriteEntity
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainLocalDataSourceTest {
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
    fun `getMainList - returns Success when OK`() = runBlocking {
        // given
        every { favoritesDao.getAll() } returns listOf()

        // when
        val sut = createSut()
        val result = sut.getMainList()

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.drinks.isEmpty()
        )
    }

    @Test
    fun `getMainById - returns Error when OKO`() = runBlocking {
        // given
        val id = "1"
        every { favoritesDao.loadAllByIds(any()) } throws Exception()

        // when
        val sut = createSut()
        val result = sut.getMainById(id)

        // then
        assertAllTrue(
            result is Resource.Error
        )
    }

    @Test
    fun `getMainById - returns Success when OK`() = runBlocking {
        // given
        val id = "1"
        every { favoritesDao.loadAllByIds(any()) } returns listOf()

        // when
        val sut = createSut()
        val result = sut.getMainById(id)

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.drinks.isEmpty()
        )
    }

    @Test
    fun `setFavoriteCocktail - Dao method is called`() = runBlocking {
        // given
        val cocktailName = "testCocktailName"
        val cocktailId = "1"

        // when
        val sut = createSut()
        sut.setFavoriteCocktail(cocktailId, cocktailName)

        // then
        verify { favoritesDao.insert(FavoriteEntity(1, cocktailName)) }
    }

    @Test
    fun `deleteFavoriteCocktail - Dao method is called`() = runBlocking {
        // given
        val cocktailId = "1"

        // when
        val sut = createSut()
        sut.deleteFavoriteCocktail(cocktailId)

        // then
        verify { favoritesDao.delete(1) }
    }

    private fun createSut() = MainLocalDataSource(favoritesDao)

}