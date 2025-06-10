package com.zioanacleto.speakeazy.ui.presentation.detail.data.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.DetailRepository
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DetailRepositoryImpl(
    private val dataSource: IngredientDataSource,
    private val dispatcherProvider: DispatcherProvider
): DetailRepository {
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