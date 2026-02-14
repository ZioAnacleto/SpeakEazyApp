package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.returningTraceSuspend
import com.zioanacleto.speakeazy.core.database.dao.CreateCocktailDao
import com.zioanacleto.speakeazy.core.database.entities.toEntity
import com.zioanacleto.speakeazy.core.database.entities.toModel
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel

class CreateCocktailLocalDataSource(
    private val createCocktailDao: CreateCocktailDao,
    private val performanceTracesManager: PerformanceTracesManager
) : CreateCocktailDataSource {
    override suspend fun getLocalCreateCocktail(): Resource<List<CreateCocktailModel>> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getLocalCreateCocktail"
            ) {
                val createCocktail = createCocktailDao.getAll().map { it.toModel() }

                AnacletoLogger.mumbling(
                    mumble = "Local create cocktail instances: $createCocktail"
                )

                Resource.Success(createCocktail)
            }
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                "Error while retrieving local create cocktail instance. Exception: $exception"
            )
            performanceTracesManager.stopTrace(
                this::class,
                "getLocalCreateCocktail"
            )

            Resource.Error(exception)
        }
    }

    override suspend fun saveLocalCreateCocktail(
        createCocktail: CreateCocktailModel
    ): Int {
        return performanceTracesManager.returningTraceSuspend(
            this::class,
            "saveLocalCreateCocktail"
        ) {
            val entity = createCocktail.toEntity()
            val response = createCocktailDao.upsert(entity).toInt()

            response
        }
    }

    override suspend fun deleteLocalCreateCocktail(uniqueId: Int?): Boolean {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "deleteLocalCreateCocktail"
            ) {
                createCocktailDao.delete(uniqueId)
                AnacletoLogger.mumbling(
                    mumble = "Local create cocktail instance deleted."
                )

                true
            }
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while deleting local create cocktail instance. Exception: $exception"
            )
            performanceTracesManager.stopTrace(
                this::class,
                "saveLocalCreateCocktail"
            )

            false
        }
    }
}