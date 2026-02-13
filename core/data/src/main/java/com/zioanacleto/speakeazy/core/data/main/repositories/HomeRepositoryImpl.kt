package com.zioanacleto.speakeazy.core.data.main.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.traceSuspend
import com.zioanacleto.speakeazy.core.data.main.datasources.HomeDataSource
import com.zioanacleto.speakeazy.core.data.main.datasources.MainDataSource
import com.zioanacleto.speakeazy.core.domain.main.HomeRepository
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepositoryImpl(
    private val networkDataSource: HomeDataSource,
    private val localDataSource: MainDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val performanceTracesManager: PerformanceTracesManager
) : HomeRepository {
    override fun getHome(): Flow<Resource<HomeModel>> =
        combine(
            flow {
                performanceTracesManager.traceSuspend(
                    this@HomeRepositoryImpl::class,
                    "getHomeSections"
                ) { emit(networkDataSource.getHomeSections()) }
            },
            flow {
                performanceTracesManager.traceSuspend(
                    this@HomeRepositoryImpl::class,
                    "getMainList"
                ) { emit(localDataSource.getMainList()) }
            }
        ) { network, local ->
            if (network is Resource.Success && local is Resource.Success) {
                Resource.Success(
                    HomeModel(
                        sections = network.data.sections.map {
                            it.copy(
                                cocktails = it.cocktails.map { drink ->
                                    drink.copy(
                                        favorite = local.data.drinks.find { localFavorite ->
                                            localFavorite.id == drink.id
                                        } != null
                                    )
                                }
                            )
                        },
                        banner = network.data.banner
                    )
                )
            } else network
        }.flowOn(dispatcherProvider.io())
}