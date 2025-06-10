package com.zioanacleto.speakeazy.ui.presentation.detail.domain

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getIngredientsList(): Flow<Resource<IngredientsModel>>
    fun getIngredientById(id: String): Flow<Resource<IngredientsModel>>
}