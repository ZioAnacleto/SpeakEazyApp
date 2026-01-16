package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel

interface CreateCocktailUploadDataSource {
    suspend fun uploadCocktail(cocktail: CreateCocktailModel): Resource<String>
}