package com.zioanacleto.speakeazy.core.data.detail.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel

interface IngredientDataSource {
    suspend fun getIngredientsList(): Resource<IngredientsModel>
    suspend fun getIngredientById(id: String): Resource<IngredientsModel>
}