package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.SearchRepository
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.testDispatcher
import com.zioanacleto.speakeazy.testResourceFlow
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var repository: SearchRepository
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
    fun test_landingUiState_whenRepositoryIsSuccess_uiStateIsSuccess() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { repository.getSearchLandingData() } returns flowOf(
            Resource.Success(
                SearchLandingModel(
                    ingredients = listOf(),
                    tags = listOf(),
                    lastQueries = listOf()
                )
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.landingUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is SearchLandingUiState.Loading,
            result is SearchLandingUiState.Success
        )
    }

    @Test
    fun test_landingUiState_whenRepositoryIsError_uiStateIsError() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { repository.getSearchLandingData() } returns flowOf(
            Resource.Error(
                Exception("testException")
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.landingUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is SearchLandingUiState.Loading,
            result is SearchLandingUiState.Error
        )
    }

    @Test
    fun test_search_whenRepositoryIsSuccess_queryUiStateIsSuccess() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        val aiMode = false
        val query = "test"
        every { repository.submitQuery(aiMode, query) } returns flowOf(
            Resource.Success(
                SearchModel()
            )
        )

        // when
        val sut = createSut()
        sut.search(aiMode, query)
        advanceUntilIdle()

        // then
        val result = sut.queryUiState.first()
        assert(result is SearchUiState.Success)
    }

    @Test
    fun test_search_whenRepositoryIsError_queryUiStateIsError() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        val aiMode = false
        val query = "test"
        every { repository.submitQuery(aiMode, query) } returns flowOf(
            Resource.Error(
                Exception("testException")
            )
        )

        // when
        val sut = createSut()
        sut.search(aiMode, query)
        advanceUntilIdle()

        // then
        val result = sut.queryUiState.first()
        assert(result is SearchUiState.Error)
    }

    @Test
    fun test_filter_whenRepositoryIsSuccess_filterUiStateIsSuccess() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { repository.submitFilter(any()) } returns flowOf(
            Resource.Success(
                MainModel(listOf())
            )
        )

        // when
        val sut = createSut()
        sut.filter(mapOf())
        advanceUntilIdle()

        // then
        val result = sut.filterUiState.first()
        assert(result is FilterUiState.Success)
    }

    @Test
    fun test_filter_whenRepositoryIsError_filterUiStateIsError() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { repository.submitFilter(any()) } returns flowOf(
            Resource.Error(
                Exception("testException")
            )
        )

        // when
        val sut = createSut()
        sut.filter(mapOf())
        advanceUntilIdle()

        // then
        val result = sut.filterUiState.first()
        assert(result is FilterUiState.Error)
    }

    private fun createSut() = SearchViewModel(repository, dispatcherProvider)
}