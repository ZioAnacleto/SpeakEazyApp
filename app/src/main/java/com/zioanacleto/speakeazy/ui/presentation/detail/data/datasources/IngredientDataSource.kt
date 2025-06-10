package com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel

interface IngredientDataSource {
    suspend fun getIngredientsList(): Resource<IngredientsModel>
    suspend fun getIngredientById(id: String): Resource<IngredientsModel>
}