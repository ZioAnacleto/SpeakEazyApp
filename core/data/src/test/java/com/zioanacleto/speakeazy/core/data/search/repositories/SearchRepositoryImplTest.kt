package com.zioanacleto.speakeazy.core.data.search.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchDataSource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    private lateinit var searchDataSource: SearchDataSource
    private lateinit var ingredientDataSource: IngredientDataSource
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        searchDataSource = mockk(relaxed = true)
        ingredientDataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
        performanceTracesManager = mockk(relaxed = true) {
            every { startTrace(any(), any()) } just runs
            every { stopTrace(any(), any()) } just runs
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_submitQuery() = runBlocking {
        // given
        coEvery { searchDataSource.querySearch(any()) } returns Resource.Success(
            SearchModel()
        )

        // when
        val sut = createSut()
        val response = sut.submitQuery("testQuery").first()

        // then
        assert(response is Resource.Success<SearchModel>)
    }

    @Test
    fun test_submitFilter() = runBlocking {
        // given
        coEvery { searchDataSource.queryFilter(any()) } returns Resource.Success(
            MainModel(drinks = listOf())
        )

        // when
        val sut = createSut()
        val response = sut.submitFilter(mapOf()).first()

        // then
        assert(response is Resource.Success<MainModel>)
    }

    private fun createSut() =
        SearchRepositoryImpl(
            searchDataSource,
            ingredientDataSource,
            dispatcherProvider,
            performanceTracesManager
        )
}