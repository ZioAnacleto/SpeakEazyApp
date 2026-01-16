package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.main.HomeRepository
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import com.zioanacleto.speakeazy.testResourceFlow
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var repository: HomeRepository
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
    fun test_homeUiState_whenRepositoryReturnsSuccess_uiStateIsSuccess() = runBlocking {
        // given
        every { repository.getHome() } returns flowOf(
            Resource.Success(
                HomeModel(listOf())
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.homeUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is HomeUiState.Loading,
            result is HomeUiState.Success
        )
    }

    @Test
    fun test_homeUiState_whenRepositoryReturnsError_uiStateIsError() = runBlocking {
        // given
        every { repository.getHome() } returns flowOf(
            Resource.Error(
                Exception("testException")
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.homeUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is HomeUiState.Loading,
            result is HomeUiState.Error
        )
    }

    private fun createSut() = MainViewModel(repository, dispatcherProvider)
}