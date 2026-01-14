package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.database.dao.CreateCocktailDao
import com.zioanacleto.speakeazy.core.database.entities.toEntity
import com.zioanacleto.speakeazy.core.database.entities.toModel
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel

class CreateCocktailLocalDataSource(
    private val createCocktailDao: CreateCocktailDao
) : CreateCocktailDataSource {
    override suspend fun getLocalCreateCocktail(): Resource<List<CreateCocktailModel>> {
        return try {
            val createCocktail = createCocktailDao.getAll().map { it.toModel() }

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
        return createCocktailDao.upsert(entity).toInt()
    }

    override suspend fun deleteLocalCreateCocktail(uniqueId: Int?): Boolean {
        return try {
            createCocktailDao.delete(uniqueId)
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