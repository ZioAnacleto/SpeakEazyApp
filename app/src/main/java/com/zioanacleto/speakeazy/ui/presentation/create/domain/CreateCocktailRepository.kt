package com.zioanacleto.speakeazy.ui.presentation.create.domain

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import kotlinx.coroutines.flow.Flow

interface CreateCocktailRepository {
    fun getCreateCocktail(): Flow<Resource<List<CreateCocktailModel>>>
    suspend fun saveCreateCocktail(createCocktail: CreateCocktailModel): Int
    suspend fun deleteCreateCocktail(uniqueId: Int?): Boolean
    fun getIngredients(): Flow<Resource<IngredientsModel>>
    suspend fun uploadCocktail(createCocktail: CreateCocktailModel): Boolean
}