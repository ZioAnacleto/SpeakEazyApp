package com.zioanacleto.speakeazy.core.data.main.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.traceSuspend
import com.zioanacleto.speakeazy.core.data.main.datasources.MainDataSource
import com.zioanacleto.speakeazy.core.domain.main.MainRepository
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepositoryImpl(
    private val networkDataSource: MainDataSource,
    private val localDataSource: MainDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val performanceTracesManager: PerformanceTracesManager
) : MainRepository {
    override fun getMainList(): Flow<Resource<MainModel>> =
        combineAndReturn(
            flow {
                performanceTracesManager.traceSuspend(
                    this@MainRepositoryImpl::class,
                    "network_getMainList"
                ) {
                    emit(networkDataSource.getMainList())
                }
            },
            flow {
                performanceTracesManager.traceSuspend(
                    this@MainRepositoryImpl::class,
                    "local_getMainList"
                ) {
                    emit(localDataSource.getMainList())
                }
            }
        )

    override fun getMainById(id: String): Flow<Resource<MainModel>> =
        combineAndReturn(
            flow {
                performanceTracesManager.traceSuspend(
                    this::class,
                    "network_getMainById"
                ) {
                    emit(networkDataSource.getMainById(id))
                }
            },
            flow {
                performanceTracesManager.traceSuspend(
                    this::class,
                    "local_getMainById"
                ) {
                    emit(localDataSource.getMainById(id))
                }
            }
        )

    override suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String) {
        performanceTracesManager.traceSuspend(
            this::class,
            "setFavoriteCocktail"
        ) {
            localDataSource.setFavoriteCocktail(cocktailId, cocktailName)
        }
    }

    override suspend fun deleteFavoriteCocktail(cocktailId: String) {
        performanceTracesManager.traceSuspend(
            this::class,
            "deleteFavoriteCocktail"
        ) {
            localDataSource.deleteFavoriteCocktail(cocktailId)
        }
    }

    override suspend fun updateVisualizations(cocktailId: String) {
        performanceTracesManager.traceSuspend(
            this::class,
            "updateVisualizations"
        ) {
            networkDataSource.updateVisualizations(cocktailId)
        }
    }

    private fun combineAndReturn(
        networkFlow: Flow<Resource<MainModel>>,
        localFlow: Flow<Resource<MainModel>>
    ): Flow<Resource<MainModel>> = combine(networkFlow, localFlow) { network, local ->
        if (network is Resource.Success && local is Resource.Success) {
            Resource.Success(
                MainModel(
                    drinks = network.data.drinks.mapNotNull { drink ->
                        drink.copy(
                            favorite = local.data.drinks.find { it.id == drink.id } != null
                        )
                    }
                )
            )
        } else network
    }.flowOn(dispatcherProvider.io())
}