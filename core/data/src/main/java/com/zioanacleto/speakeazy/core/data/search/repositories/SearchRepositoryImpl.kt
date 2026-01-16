package com.zioanacleto.speakeazy.core.data.search.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchDataSource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.SearchRepository
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val ingredientDataSource: IngredientDataSource,
    private val dispatcherProvider: DispatcherProvider
) : SearchRepository {
    override fun submitQuery(query: String): Flow<Resource<SearchModel>> =
        flow {
            emit(
                searchDataSource.querySearch(query)
            )
        }.flowOn(dispatcherProvider.io())

    override fun getSearchLandingData(): Flow<Resource<SearchLandingModel>> =
        combine(
            flow { emit(ingredientDataSource.getIngredientsList()) },
            flow { emit(searchDataSource.getTags()) }
        ) { ingredients, tags ->
            if (ingredients is Resource.Success && tags is Resource.Success) {
                Resource.Success(
                    data = SearchLandingModel(
                        ingredients = ingredients.data.ingredients,
                        tags = tags.data.tags
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
        flow {
            emit(
                searchDataSource.queryFilter(filters)
            )
        }.flowOn(dispatcherProvider.io())
}