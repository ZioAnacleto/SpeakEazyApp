package com.zioanacleto.speakeazy.core.domain.favorites

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteCocktails(): Flow<Resource<FavoritesModel>>
}