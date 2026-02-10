package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.core.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel

class FavoritesLocalDataSource(
    private val favoritesDao: FavoritesDao,
    private val performanceTracesManager: PerformanceTracesManager
) : FavoritesDataSource {
    override suspend fun getCocktails(): Resource<FavoritesModel> {
        performanceTracesManager.startTrace(
            this::class,
            "getCocktails"
        )
        return try {
            val favorites = favoritesDao.getAll().map { it.toDrinkModel() }
            performanceTracesManager.stopTrace(
                this::class,
                "getCocktails"
            )
            Resource.Success(
                FavoritesModel(favorites)
            )
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getCocktails"
            )
            Resource.Error(exception)
        }
    }
}