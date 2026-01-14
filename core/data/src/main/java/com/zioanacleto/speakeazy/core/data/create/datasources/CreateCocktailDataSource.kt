package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel

interface CreateCocktailDataSource {
    suspend fun getLocalCreateCocktail(): Resource<List<CreateCocktailModel>>
    suspend fun saveLocalCreateCocktail(
        createCocktail: CreateCocktailModel
    ) : Int
    suspend fun deleteLocalCreateCocktail(uniqueId: Int?): Boolean
}