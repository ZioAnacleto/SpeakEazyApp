package com.zioanacleto.speakeazy.core.data.detail.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.database.dao.IngredientDao
import com.zioanacleto.speakeazy.core.database.entities.toIngredientModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel

class IngredientLocalDataSource(
    private val ingredientDao: IngredientDao
) : IngredientDataSource {
    override suspend fun getIngredientsList(): Resource<IngredientsModel> {
        return try {
            val list = ingredientDao.getAll().map { it.toIngredientModel() }
            Resource.Success(
                IngredientsModel(list)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    override suspend fun getIngredientById(id: String): Resource<IngredientsModel> {
        return try {
            val ingredient = ingredientDao.loadAllByIds(intArrayOf(id.toInt()))
                .map { it.toIngredientModel() }
            Resource.Success(
                IngredientsModel(ingredient)
            )
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }
}