package com.zioanacleto.speakeazy.core.data.search.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.traceSuspend
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchQueriesDataSource
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.SearchRepository
import com.zioanacleto.speakeazy.core.domain.search.model.QueryModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.core.domain.search.model.TagsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val ingredientDataSource: IngredientDataSource,
    private val searchQueriesDataSource: SearchQueriesDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val performanceTracesManager: PerformanceTracesManager
) : SearchRepository {
    override fun submitQuery(aiSearchMode: Boolean, query: String): Flow<Resource<SearchModel>> =
        flowOnDispatcher {
            performanceTracesManager.traceSuspend(
                this@SearchRepositoryImpl::class,
                "submitQuery"
            ) {
                emit(
                    searchDataSource.querySearch(aiSearchMode, query)
                )
            }
        }

    override fun getSearchLandingData(): Flow<Resource<SearchLandingModel>> =
        combine(
            ingredientsFlow,
            tagsFlow,
            searchQueriesFlow
        ) { ingredients, tags, queries ->
            if (ingredients is Resource.Success && tags is Resource.Success) {
                Resource.Success(
                    data = SearchLandingModel(
                        ingredients = ingredients.data.ingredients,
                        tags = tags.data.tags,
                        lastQueries = (queries as? Resource.Success)?.data ?: listOf()
                    )
                )
            } else if (ingredients is Resource.Error || tags is Resource.Error) {
                Resource.Error(Exception("Error"))
            } else {
                Resource.Loading
            }
        }.flowOn(dispatcherProvider.io())

    override fun submitFilter(
        filters: Map<SearchFilterModel, List<String>>
    ): Flow<Resource<MainModel>> =
        flowOnDispatcher {
            performanceTracesManager.traceSuspend(
                this@SearchRepositoryImpl::class,
                "submitFilter"
            ) {
                emit(
                    searchDataSource.queryFilter(filters)
                )
            }
        }

    override suspend fun addQueryToLocalDatabase(query: String) {
        performanceTracesManager.traceSuspend(
            this@SearchRepositoryImpl::class,
            "addQueryToLocalDatabase"
        ) { searchQueriesDataSource.insertQuery(query) }
    }

    private val ingredientsFlow: Flow<Resource<IngredientsModel>> =
        flow {
            performanceTracesManager.traceSuspend(
                this@SearchRepositoryImpl::class,
                "ingredientDataSource_getSearchLandingData"
            ) {
                emit(ingredientDataSource.getIngredientsList())
            }
        }

    private val tagsFlow: Flow<Resource<TagsModel>> =
        flow {
            performanceTracesManager.traceSuspend(
                this@SearchRepositoryImpl::class,
                "searchDataSource_getSearchLandingData"
            ) {
                emit(searchDataSource.getTags())
            }
        }

    private val searchQueriesFlow: Flow<Resource<List<QueryModel>>> =
        flow {
            performanceTracesManager.traceSuspend(
                this@SearchRepositoryImpl::class,
                "searchQueriesDataSource_getSearchLandingData"
            ) {
                emit(searchQueriesDataSource.getLastQueries())
            }
        }.distinctUntilChanged()

    private fun <T> flowOnDispatcher(
        collector: suspend FlowCollector<Resource<T>>.() -> Unit
    ) = flow { collector() }.flowOn(dispatcherProvider.io())
}