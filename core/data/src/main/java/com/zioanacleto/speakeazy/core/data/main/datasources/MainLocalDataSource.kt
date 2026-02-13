package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.returningTraceSuspend
import com.zioanacleto.speakeazy.core.analytics.traces.trace
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.core.database.entities.FavoriteEntity
import com.zioanacleto.speakeazy.core.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel

class MainLocalDataSource(
    private val favoritesDao: FavoritesDao,
    private val performanceTracesManager: PerformanceTracesManager
) : MainDataSource {
    override suspend fun getMainList(): Resource<MainModel> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getMainList"
            ) {
                val list = favoritesDao.getAll().map { it.toDrinkModel() }

                Resource.Success(
                    MainModel(list)
                )
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getMainList"
            )
            Resource.Error(exception)
        }
    }

    override suspend fun getMainById(id: String): Resource<MainModel> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getMainById"
            ) {
                val list = favoritesDao.loadAllByIds(intArrayOf(id.toInt())).map {
                    it.toDrinkModel()
                }
                Resource.Success(
                    MainModel(list)
                )
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getMainById"
            )
            Resource.Error(exception)
        }
    }

    override suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String) {
        performanceTracesManager.trace(
            this::class,
            "setFavoriteCocktail"
        ) {
            favoritesDao.insert(
                FavoriteEntity(cocktailId.toInt(), cocktailName)
            )
        }
    }

    override suspend fun deleteFavoriteCocktail(cocktailId: String) {
        performanceTracesManager.trace(
            this::class,
            "deleteFavoriteCocktail"
        ) {
            favoritesDao.delete(cocktailId.toInt())
        }
    }

    override suspend fun updateVisualizations(
        cocktailId: String
    ) { /*do nothing here*/ }
}