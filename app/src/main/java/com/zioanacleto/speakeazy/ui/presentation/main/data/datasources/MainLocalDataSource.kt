package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.database.entities.FavoriteEntity
import com.zioanacleto.speakeazy.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel

class MainLocalDataSource(
    private val favoritesDao: FavoritesDao
) : MainDataSource {
    override suspend fun getMainList(): Resource<MainModel> {
        return try {
            val list = favoritesDao.getAll().map { it.toDrinkModel() }

            Resource.Success(
                MainModel(list)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    override suspend fun getMainById(id: String): Resource<MainModel> {
        return try {
            val list = favoritesDao.loadAllByIds(intArrayOf(id.toInt())).map {
                it.toDrinkModel()
            }
            Resource.Success(
                MainModel(list)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    override suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String) {
        favoritesDao.insert(
            FavoriteEntity(cocktailId.toInt(), cocktailName)
        )
    }

    override suspend fun deleteFavoriteCocktail(cocktailId: String) {
        favoritesDao.delete(cocktailId.toInt())
    }

    override suspend fun updateVisualizations(
        cocktailId: String
    ) { /*do nothing here*/
    }
}