package com.zioanacleto.speakeazy.core.data.detail.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.returningTraceSuspend
import com.zioanacleto.speakeazy.core.database.dao.IngredientDao
import com.zioanacleto.speakeazy.core.database.entities.toIngredientModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel

class IngredientLocalDataSource(
    private val ingredientDao: IngredientDao,
    private val performanceTracesManager: PerformanceTracesManager
) : IngredientDataSource {
    override suspend fun getIngredientsList(): Resource<IngredientsModel> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getIngredientsList"
            ) {
                val list = ingredientDao.getAll().map { it.toIngredientModel() }
                Resource.Success(
                    IngredientsModel(list)
                )
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientsList"
            )
            Resource.Error(exception)
        }
    }

    override suspend fun getIngredientById(id: String): Resource<IngredientsModel> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getIngredientById"
            ) {
                val ingredient = ingredientDao.loadAllByIds(intArrayOf(id.toInt()))
                    .map { it.toIngredientModel() }

                Resource.Success(
                    IngredientsModel(ingredient)
                )
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientById"
            )
            Resource.Error(exception)
        }
    }
}