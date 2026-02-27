package com.zioanacleto.speakeazy.core.data.search.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchQueriesDataSource
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.core.domain.search.model.TagsModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
    private lateinit var searchQueriesDataSource: SearchQueriesDataSource
    private lateinit var ingredientDataSource: IngredientDataSource
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        searchDataSource = mockk(relaxed = true)
        searchQueriesDataSource = mockk(relaxed = true)
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
    fun `submitQuery - returns Success`() = runBlocking {
        // given
        coEvery { searchDataSource.querySearch(any(), any()) } returns Resource.Success(
            SearchModel()
        )

        // when
        val sut = createSut()
        val response = sut.submitQuery(false, "testQuery").first()

        // then
        assert(response is Resource.Success<SearchModel>)
    }

    @Test
    fun `getSearchLandingData - when ingredients and tags are OK return success`() = runBlocking {
        // given
        coEvery {
            ingredientDataSource.getIngredientsList()
        } returns Resource.Success(
            IngredientsModel(listOf())
        )
        coEvery {
            searchDataSource.getTags()
        } returns Resource.Success(
            TagsModel(listOf())
        )
        coEvery {
            searchQueriesDataSource.getLastQueries()
        } returns Resource.Success(listOf())

        // when
        val sut = createSut()
        val response = sut.getSearchLandingData().first()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.tags.isEmpty(),
            response.data.ingredients.isEmpty(),
            response.data.lastQueries.isEmpty(),
        )
    }

    @Test
    fun `getSearchLandingData - when ingredients is KO return Error`() = runBlocking {
        // given
        coEvery {
            ingredientDataSource.getIngredientsList()
        } returns Resource.Error(Exception())
        coEvery {
            searchDataSource.getTags()
        } returns Resource.Success(
            TagsModel(listOf())
        )
        coEvery {
            searchQueriesDataSource.getLastQueries()
        } returns Resource.Success(listOf())

        // when
        val sut = createSut()
        val response = sut.getSearchLandingData().first()

        // then
        assert(response is Resource.Error)
    }

    @Test
    fun `getSearchLandingData - when tags is KO return Error`() = runBlocking {
        // given
        coEvery {
            ingredientDataSource.getIngredientsList()
        } returns Resource.Success(
            IngredientsModel(listOf())
        )
        coEvery {
            searchDataSource.getTags()
        } returns Resource.Error(Exception())
        coEvery {
            searchQueriesDataSource.getLastQueries()
        } returns Resource.Success(listOf())

        // when
        val sut = createSut()
        val response = sut.getSearchLandingData().first()

        // then
        assert(response is Resource.Error)
    }

    @Test
    fun `submitFilter - returns Success`() = runBlocking {
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

    @Test
    fun `addQueryToLocalDatabase - should call data source method`() = runBlocking{
        // given
        val query = "testQuery"

        // when
        val sut = createSut()
        sut.addQueryToLocalDatabase(query)

        // then
        coVerify { searchQueriesDataSource.insertQuery(query) }
    }

    private fun createSut() =
        SearchRepositoryImpl(
            searchDataSource,
            ingredientDataSource,
            searchQueriesDataSource,
            dispatcherProvider,
            performanceTracesManager
        )
}