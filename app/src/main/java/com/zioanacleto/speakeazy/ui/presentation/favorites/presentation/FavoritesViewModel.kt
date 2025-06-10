package com.zioanacleto.speakeazy.ui.presentation.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    favoritesRepository: FavoritesRepository
) : ViewModel() {

    val favoritesUiState: Flow<FavoritesUiState> =
        favoritesRepository.getFavoriteCocktails()
            .mapResourceAsFavoritesUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FavoritesUiState.Loading
            )
}