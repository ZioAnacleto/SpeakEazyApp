package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources

import android.content.Context
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.database.SpeakEazyRoomDatabase
import com.zioanacleto.speakeazy.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel

class FavoritesLocalDataSource(
    context: Context
) : FavoritesDataSource {
    private val database: SpeakEazyRoomDatabase = SpeakEazyRoomDatabase.getDatabase(context)

    override suspend fun getCocktails(): Resource<FavoritesModel> {
        return try {
            val favorites = database.favoritesDao().getAll().map { it.toDrinkModel() }
            Resource.Success(
                FavoritesModel(favorites)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }
}