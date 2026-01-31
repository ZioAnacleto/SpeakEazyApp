package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel

interface FavoritesDataSource {
    suspend fun getCocktails(): Resource<FavoritesModel>
}