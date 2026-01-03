package com.zioanacleto.speakeazy.ui.presentation.search.data.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.search.data.datasources.SearchDataSource
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchRepository
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchLandingModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
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
            if(ingredients is Resource.Success && tags is Resource.Success) {
                Resource.Success(
                    data = SearchLandingModel(
                        ingredients = ingredients.data.ingredients,
                        tags = tags.data.tags
                    )
                )
            } else if(ingredients is Resource.Error || tags is Resource.Error) {
                Resource.Error(Exception("Error"))
            } else {
                Resource.Loading
            }
        }.flowOn(dispatcherProvider.io())

    override fun submitFilter(
        selectedFilters: Map<SearchFilterItem, List<String>>
    ): Flow<Resource<MainModel>> =
        flow {
            emit(
                searchDataSource.queryFilter(selectedFilters)
            )
        }.flowOn(dispatcherProvider.io())
}