package com.zioanacleto.speakeazy.ui.presentation.create.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel

interface CreateCocktailUploadDataSource {
    suspend fun uploadCocktail(cocktail: CreateCocktailModel): Resource<String>
}