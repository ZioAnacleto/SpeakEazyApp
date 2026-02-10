package com.zioanacleto.speakeazy.core.data.detail.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.database.dao.IngredientDao
import com.zioanacleto.speakeazy.core.database.entities.toIngredientModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel

class IngredientLocalDataSource(
    private val ingredientDao: IngredientDao,
    private val performanceTracesManager: PerformanceTracesManager
) : IngredientDataSource {
    override suspend fun getIngredientsList(): Resource<IngredientsModel> {
        performanceTracesManager.startTrace(
            this::class,
            "getIngredientsList"
        )
        return try {
            val list = ingredientDao.getAll().map { it.toIngredientModel() }
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientsList"
            )
            Resource.Success(
                IngredientsModel(list)
            )
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientsList"
            )
            Resource.Error(exception)
        }
    }

    override suspend fun getIngredientById(id: String): Resource<IngredientsModel> {
        performanceTracesManager.startTrace(
            this::class,
            "getIngredientById"
        )
        return try {
            val ingredient = ingredientDao.loadAllByIds(intArrayOf(id.toInt()))
                .map { it.toIngredientModel() }
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientById"
            )
            Resource.Success(
                IngredientsModel(ingredient)
            )
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientById"
            )
            Resource.Error(exception)
        }
    }
}