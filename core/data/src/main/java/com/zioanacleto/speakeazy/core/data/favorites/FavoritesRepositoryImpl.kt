package com.zioanacleto.speakeazy.core.data.favorites

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.favorites.datasources.FavoritesDataSource
import com.zioanacleto.speakeazy.core.domain.favorites.FavoritesRepository
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoritesRepositoryImpl(
    private val networkDataSource: FavoritesDataSource,
    private val localDataSource: FavoritesDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val performanceTracesManager: PerformanceTracesManager
) : FavoritesRepository {
    override fun getFavoriteCocktails(): Flow<Resource<FavoritesModel>> =
        combine(
            flow {
                performanceTracesManager.startTrace(
                    this@FavoritesRepositoryImpl::class,
                    "network_getCocktails"
                )
                emit(networkDataSource.getCocktails())
                performanceTracesManager.stopTrace(
                    this@FavoritesRepositoryImpl::class,
                    "network_getCocktails"
                )
            },
            flow {
                performanceTracesManager.startTrace(
                    this@FavoritesRepositoryImpl::class,
                    "local_getCocktails"
                )
                emit(localDataSource.getCocktails())
                performanceTracesManager.stopTrace(
                    this@FavoritesRepositoryImpl::class,
                    "local_getCocktails"
                )
            }
        ) { network, local ->
            if (network is Resource.Success && local is Resource.Success) {
                Resource.Success(
                    FavoritesModel(
                        favorites = network.data.favorites.mapNotNull { drink ->
                            drink.copy(
                                favorite = local.data.favorites.find { it.id == drink.id } != null
                            )
                        }.filter { it.favorite }
                    )
                )
            } else network
        }.flowOn(dispatcherProvider.io())
}