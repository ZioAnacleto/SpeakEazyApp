package com.zioanacleto.speakeazy.ui.presentation.detail.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.detail.DetailRepository
import com.zioanacleto.speakeazy.core.domain.main.MainRepository
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.testDispatcher
import com.zioanacleto.speakeazy.testResourceFlow
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailViewModelTest {

    private lateinit var detailRepository: DetailRepository
    private lateinit var mainRepository: MainRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        detailRepository = mockk(relaxed = true)
        mainRepository = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_detailUiState_whenRepositoryReturnsSuccess_uiStateIsLoadingThenSuccess() =
        runTest {
            // given
            dispatcherProvider = testDispatcher()
            val cocktailId = "1"
            every { mainRepository.getMainById(any()) } returns flowOf(
                Resource.Success(
                    MainModel(drinks = listOf())
                )
            )

            val sut = createSut()

            // when
            val (resultLoading, result) = sut.detailUiState(cocktailId).testResourceFlow()

            // then
            assertAllTrue(
                resultLoading is DetailUiState.Loading,
                result is DetailUiState.Success
            )
        }

    @Test
    fun test_detailUiState_whenRepositoryReturnsError_uiStateIsLoadingThenError() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        val cocktailId = "1"
        every { mainRepository.getMainById(any()) } returns flowOf(
            Resource.Error(
                Exception("testException")
            )
        )

        val sut = createSut()

        // when
        val (resultLoading, result) = sut.detailUiState(cocktailId).testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is DetailUiState.Loading,
            result is DetailUiState.Error
        )
    }

    @Test
    fun test_setFavoriteCocktail_verifyRepositoryCall() {
        // given
        val cocktailId = "1"
        val cocktailName = "test"
        val sut = createSut()

        // when
        sut.setFavoriteCocktail(cocktailId, cocktailName)

        // then
        coVerify { mainRepository.setFavoriteCocktail(cocktailId, cocktailName) }
    }

    @Test
    fun test_deleteFavoriteCocktail_verifyRepositoryCall() {
        // given
        val cocktailId = "1"
        val sut = createSut()

        // when
        sut.deleteFavoriteCocktail(cocktailId)

        // then
        coVerify { mainRepository.deleteFavoriteCocktail(cocktailId) }
    }

    @Test
    fun test_updateVisualizations_verifyRepositoryCall() {
        // given
        val cocktailId = "1"
        val sut = createSut()

        // when
        sut.updateVisualizations(cocktailId)

        // then
        coVerify { mainRepository.updateVisualizations(cocktailId) }
    }

    private fun createSut() = DetailViewModel(
        detailRepository = detailRepository,
        mainRepository = mainRepository,
        dispatcherProvider = dispatcherProvider
    )
}