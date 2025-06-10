package com.zioanacleto.speakeazy.ui.presentation.favorites.domain

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteCocktails(): Flow<Resource<FavoritesModel>>
}