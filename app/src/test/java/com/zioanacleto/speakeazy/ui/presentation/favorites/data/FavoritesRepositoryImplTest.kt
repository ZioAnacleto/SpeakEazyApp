package com.zioanacleto.speakeazy.ui.presentation.favorites.data

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources.FavoritesDataSource
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoritesRepositoryImplTest {

    private lateinit var localDataSource: FavoritesDataSource
    private lateinit var networkDataSource: FavoritesDataSource
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        localDataSource = mockk(relaxed = true)
        networkDataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getFavoriteCocktails_whenLocalAndNetworkSuccess_returnSuccess() = runBlocking {
        // given
        coEvery { localDataSource.getCocktails() } returns Resource.Success(FavoritesModel(listOf()))
        coEvery { networkDataSource.getCocktails() } returns Resource.Success(FavoritesModel(listOf()))

        val sut = createSut()

        // when
        val result = sut.getFavoriteCocktails().first()

        // then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun test_getFavoriteCocktails_whenLocalErrorAndNetworkSuccess_returnNetwork() = runBlocking {
        // given
        coEvery { localDataSource.getCocktails() } returns Resource.Error(Exception("Test"))
        coEvery { networkDataSource.getCocktails() } returns Resource.Success(
            FavoritesModel(
                listOf(
                    DrinkModel(
                        id = "1",
                        name = "Test",
                        favorite = true
                    )
                )
            )
        )

        val sut = createSut()

        // when
        val result = sut.getFavoriteCocktails().first()

        // then
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.favorites.isNotEmpty())
    }

    @Test
    fun test_getFavoriteCocktails_whenLocalSuccessAndNetworkError_returnNetwork() = runBlocking {
        // given
        coEvery { networkDataSource.getCocktails() } returns Resource.Error(Exception("Test"))
        coEvery { localDataSource.getCocktails() } returns Resource.Success(
            FavoritesModel(
                listOf(
                    DrinkModel(
                        id = "1",
                        name = "Test",
                        favorite = true
                    )
                )
            )
        )

        val sut = createSut()

        // when
        val result = sut.getFavoriteCocktails().first()

        // then
        assertTrue(result is Resource.Error)
    }

    private fun createSut() = FavoritesRepositoryImpl(
        networkDataSource,
        localDataSource,
        dispatcherProvider
    )
}