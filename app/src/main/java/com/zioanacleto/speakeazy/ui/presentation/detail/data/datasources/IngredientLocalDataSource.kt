package com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.database.dao.IngredientDao
import com.zioanacleto.speakeazy.database.entities.toIngredientModel
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel

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