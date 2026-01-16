package com.zioanacleto.speakeazy.core.domain.create

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import kotlinx.coroutines.flow.Flow

interface CreateCocktailRepository {
    fun getCreateCocktail(): Flow<Resource<List<CreateCocktailModel>>>
    suspend fun saveCreateCocktail(createCocktail: CreateCocktailModel): Int
    suspend fun deleteCreateCocktail(uniqueId: Int?): Boolean
    fun getIngredients(): Flow<Resource<IngredientsModel>>
    suspend fun uploadCocktail(createCocktail: CreateCocktailModel): Boolean
}