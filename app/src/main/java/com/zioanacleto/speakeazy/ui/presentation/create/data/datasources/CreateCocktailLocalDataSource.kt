package com.zioanacleto.speakeazy.ui.presentation.create.data.datasources

import android.content.Context
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.database.SpeakEazyRoomDatabase
import com.zioanacleto.speakeazy.database.entities.toModel
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.toEntity

class CreateCocktailLocalDataSource(
    context: Context
) : CreateCocktailDataSource {
    private val database: SpeakEazyRoomDatabase = SpeakEazyRoomDatabase.getDatabase(context)

    override suspend fun getLocalCreateCocktail(): Resource<List<CreateCocktailModel>> {
        return try {
            val createCocktail = database.createCocktailDao().getAll().map { it.toModel() }

            AnacletoLogger.mumbling(
                mumble = "Local create cocktail instances: $createCocktail"
            )

            Resource.Success(createCocktail)
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                "Error while retrieving local create cocktail instance. Exception: $exception"
            )
            Resource.Error(exception)
        }
    }

    override suspend fun saveLocalCreateCocktail(
        createCocktail: CreateCocktailModel
    ): Int {
        val entity = createCocktail.toEntity()
        return database.createCocktailDao().upsert(entity).toInt()
    }

    override suspend fun deleteLocalCreateCocktail(uniqueId: Int?): Boolean {
        return try {
            database.createCocktailDao().delete(uniqueId)
            AnacletoLogger.mumbling(
                mumble = "Local create cocktail instance deleted."
            )

            true
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while deleting local create cocktail instance. Exception: $exception"
            )

            false
        }
    }
}