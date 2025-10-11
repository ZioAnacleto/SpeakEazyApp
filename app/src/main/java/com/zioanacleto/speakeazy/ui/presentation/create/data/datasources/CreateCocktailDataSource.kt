package com.zioanacleto.speakeazy.ui.presentation.create.data.datasources

import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import com.zioanacleto.buffa.events.Resource

interface CreateCocktailDataSource {
    suspend fun getLocalCreateCocktail(): Resource<List<CreateCocktailModel>>
    suspend fun saveLocalCreateCocktail(
        createCocktail: CreateCocktailModel
    ) : Int
    suspend fun deleteLocalCreateCocktail(uniqueId: Int?): Boolean
}