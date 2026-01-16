package com.zioanacleto.speakeazy.ui.presentation.favorites.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface FavoritesUiState {
    data class Success(val favorites: FavoritesModel) : FavoritesUiState
    data class Error(val exception: Throwable?) : FavoritesUiState
    data object Loading : FavoritesUiState
}

fun Flow<Resource<FavoritesModel>>.mapResourceAsFavoritesUiState() = this.map { favoritesResource ->
    when (favoritesResource) {
        is Resource.Success -> FavoritesUiState.Success(favoritesResource.data)
        is Resource.Error -> FavoritesUiState.Error(favoritesResource.exception)
        is Resource.Loading -> FavoritesUiState.Loading
    }
}