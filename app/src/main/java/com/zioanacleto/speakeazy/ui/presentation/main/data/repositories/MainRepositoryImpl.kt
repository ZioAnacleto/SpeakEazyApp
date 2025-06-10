package com.zioanacleto.speakeazy.ui.presentation.main.data.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.MainDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.MainRepository
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepositoryImpl(
    private val networkDataSource: MainDataSource,
    private val localDataSource: MainDataSource,
    private val dispatcherProvider: DispatcherProvider
): MainRepository {
    override fun getMainList(): Flow<Resource<MainModel>> =
        combine(
            flow { emit(networkDataSource.getMainList()) },
            flow { emit(localDataSource.getMainList()) }
        ) { network, local ->
            if(network is Resource.Success && local is Resource.Success) {
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

    override fun getMainById(id: String): Flow<Resource<MainModel>> =
        combine(
            flow { emit(networkDataSource.getMainById(id)) },
            flow { emit(localDataSource.getMainById(id)) }
        ) { network, local ->
            if(network is Resource.Success && local is Resource.Success) {
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

    override suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String) {
        localDataSource.setFavoriteCocktail(cocktailId, cocktailName)
    }

    override suspend fun deleteFavoriteCocktail(cocktailId: String) {
        localDataSource.deleteFavoriteCocktail(cocktailId)
    }

    override suspend fun updateVisualizations(
        cocktailId: String
    ) {
        networkDataSource.updateVisualizations(cocktailId)
    }
}