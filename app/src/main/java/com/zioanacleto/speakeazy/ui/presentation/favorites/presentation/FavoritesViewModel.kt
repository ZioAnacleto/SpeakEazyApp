package com.zioanacleto.speakeazy.ui.presentation.favorites.presentation

import com.zioanacleto.buffa.base.BaseViewModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    favoritesRepository: FavoritesRepository,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    val favoritesUiState: Flow<FavoritesUiState> =
        favoritesRepository.getFavoriteCocktails()
            .mapResourceAsFavoritesUiState()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FavoritesUiState.Loading
            )
}