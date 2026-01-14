package com.zioanacleto.speakeazy.ui.presentation.detail.presentation

import com.zioanacleto.buffa.base.BaseViewModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.core.domain.detail.DetailRepository
import com.zioanacleto.speakeazy.core.domain.main.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val detailRepository: DetailRepository,
    private val mainRepository: MainRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    val detailUiState: (String) -> Flow<DetailUiState> = { cocktailId ->
        mainRepository.getMainById(cocktailId)
            .mapResourceAsDetailUiState()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DetailUiState.Loading
            )
    }

    fun setFavoriteCocktail(cocktailId: String, cocktailName: String) {
        coroutineScope.launch(dispatcherProvider.io()) {
            mainRepository.setFavoriteCocktail(cocktailId, cocktailName)
        }
    }

    fun deleteFavoriteCocktail(cocktailId: String) {
        coroutineScope.launch(dispatcherProvider.io()) {
            mainRepository.deleteFavoriteCocktail(cocktailId)
        }
    }

    fun updateVisualizations(
        cocktailId: String
    ) {
        coroutineScope.launch(dispatcherProvider.io()) {
            mainRepository.updateVisualizations(cocktailId)
        }
    }
}