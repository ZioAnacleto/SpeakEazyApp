package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel

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