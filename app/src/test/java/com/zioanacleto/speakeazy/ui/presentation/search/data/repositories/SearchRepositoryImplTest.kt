package com.zioanacleto.speakeazy.ui.presentation.search.data.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.search.data.datasources.SearchDataSource
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    private lateinit var searchDataSource: SearchDataSource
    private lateinit var ingredientDataSource: IngredientDataSource
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        searchDataSource = mockk(relaxed = true)
        ingredientDataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
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
        coEvery { searchDataSource.queryFilter(any(),any()) } returns Resource.Success(
            MainModel(drinks = listOf())
        )

        // when
        val sut = createSut()
        val response = sut.submitFilter(SearchFilterItem.INGREDIENTS, listOf()).first()

        // then
        assert(response is Resource.Success<MainModel>)
    }

    private fun createSut() = SearchRepositoryImpl(searchDataSource, ingredientDataSource, dispatcherProvider)
}