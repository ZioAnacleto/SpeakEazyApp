package com.zioanacleto.speakeazy.core.domain.detail

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getIngredientsList(): Flow<Resource<IngredientsModel>>
    fun getIngredientById(id: String): Flow<Resource<IngredientsModel>>
}