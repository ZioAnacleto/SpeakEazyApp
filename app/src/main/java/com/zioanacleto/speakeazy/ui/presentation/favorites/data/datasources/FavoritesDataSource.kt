package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel

interface FavoritesDataSource {
    suspend fun getCocktails(): Resource<FavoritesModel>
}