package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import android.content.Context
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.database.SpeakEazyRoomDatabase
import com.zioanacleto.speakeazy.database.entities.FavoriteEntity
import com.zioanacleto.speakeazy.database.entities.toDrinkModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel

class MainLocalDataSource(
    context: Context
) : MainDataSource {
    private val database: SpeakEazyRoomDatabase = SpeakEazyRoomDatabase.getDatabase(context)

    override suspend fun getMainList(): Resource<MainModel> {
        return try {
            val list = database.favoritesDao().getAll().map { it.toDrinkModel() }

            Resource.Success(
                MainModel(list)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    override suspend fun getMainById(id: String): Resource<MainModel> {
        return try {
            val list = database.favoritesDao().loadAllByIds(intArrayOf(id.toInt())).map {
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
        database.favoritesDao().insert(
            FavoriteEntity(cocktailId.toInt(), cocktailName)
        )
    }

    override suspend fun deleteFavoriteCocktail(cocktailId: String) {
        database.favoritesDao().delete(cocktailId.toInt())
    }

    override suspend fun updateVisualizations(
        cocktailId: String
    ) { /*do nothing here*/ }
}