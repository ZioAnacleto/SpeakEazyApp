package com.zioanacleto.speakeazy.ui.presentation.favorites.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.testResourceFlow
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.FavoritesRepository
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoritesViewModelTest {

    private lateinit var repository: FavoritesRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_favoritesUiState_whenRepositorySuccess_returnSuccess() = runBlocking {
        // given
        every { repository.getFavoriteCocktails() } returns flowOf(
            Resource.Success(
                FavoritesModel(
                    listOf()
                )
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.favoritesUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is FavoritesUiState.Loading,
            result is FavoritesUiState.Success
        )
    }

    @Test
    fun test_favoritesUiState_whenRepositoryError_returnError() = runBlocking {
        // given
        every { repository.getFavoriteCocktails() } returns flowOf(
            Resource.Error(
                Exception("testException")
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.favoritesUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is FavoritesUiState.Loading,
            result is FavoritesUiState.Error
        )
    }

    private fun createSut() = FavoritesViewModel(repository, dispatcherProvider)
}