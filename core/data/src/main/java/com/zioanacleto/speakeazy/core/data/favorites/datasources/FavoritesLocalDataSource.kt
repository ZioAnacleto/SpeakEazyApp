package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.core.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel

class FavoritesLocalDataSource(
    private val favoritesDao: FavoritesDao
) : FavoritesDataSource {
    override suspend fun getCocktails(): Resource<FavoritesModel> {
        return try {
            val favorites = favoritesDao.getAll().map { it.toDrinkModel() }
            Resource.Success(
                FavoritesModel(favorites)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }
}