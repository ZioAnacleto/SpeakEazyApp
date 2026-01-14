package com.zioanacleto.speakeazy.core.data.detail.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.domain.detail.DetailRepository
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DetailRepositoryImpl(
    private val dataSource: IngredientDataSource,
    private val dispatcherProvider: DispatcherProvider
) : DetailRepository {
    override fun getIngredientsList(): Flow<Resource<IngredientsModel>> =
        flow {
            emit(
                dataSource.getIngredientsList()
            )
        }.flowOn(dispatcherProvider.io())

    override fun getIngredientById(id: String): Flow<Resource<IngredientsModel>> =
        flow {
            emit(
                dataSource.getIngredientById(id)
            )
        }.flowOn(dispatcherProvider.io())
}