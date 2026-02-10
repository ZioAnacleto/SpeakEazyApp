package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoritesLocalDataSourceTest {
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        performanceTracesManager = mockk(relaxed = true)
        every { performanceTracesManager.startTrace(any(), any()) } just runs
        every { performanceTracesManager.stopTrace(any(), any()) } just runs
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

    private fun createSut() = FavoritesLocalDataSource(favoritesDao, performanceTracesManager)

}