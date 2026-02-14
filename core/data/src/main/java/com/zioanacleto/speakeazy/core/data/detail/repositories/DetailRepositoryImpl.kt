package com.zioanacleto.speakeazy.core.data.detail.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.traceSuspend
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.domain.detail.DetailRepository
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DetailRepositoryImpl(
    private val dataSource: IngredientDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val performanceTracesManager: PerformanceTracesManager
) : DetailRepository {
    override fun getIngredientsList(): Flow<Resource<IngredientsModel>> =
        flow {
            performanceTracesManager.traceSuspend(
                this@DetailRepositoryImpl::class,
                "getIngredientsList"
            ) {
                emit(
                    dataSource.getIngredientsList()
                )
            }
        }.flowOn(dispatcherProvider.io())

    override fun getIngredientById(id: String): Flow<Resource<IngredientsModel>> =
        flow {
            performanceTracesManager.traceSuspend(
                this@DetailRepositoryImpl::class,
                "getIngredientById"
            ) {
                emit(
                    dataSource.getIngredientById(id)
                )
            }
        }.flowOn(dispatcherProvider.io())
}