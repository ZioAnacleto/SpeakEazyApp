package com.zioanacleto.speakeazy.ui.presentation.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.DetailRepository
import com.zioanacleto.speakeazy.ui.presentation.main.domain.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val detailRepository: DetailRepository,
    private val mainRepository: MainRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val detailUiState: (String) -> Flow<DetailUiState> = { cocktailId ->
        mainRepository.getMainById(cocktailId)
            .mapResourceAsDetailUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DetailUiState.Loading
            )
    }

    fun setFavoriteCocktail(cocktailId: String, cocktailName: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            mainRepository.setFavoriteCocktail(cocktailId, cocktailName)
        }
    }

    fun deleteFavoriteCocktail(cocktailId: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            mainRepository.deleteFavoriteCocktail(cocktailId)
        }
    }

    fun updateVisualizations(
        cocktailId: String
    ) {
        viewModelScope.launch(dispatcherProvider.io()) {
            mainRepository.updateVisualizations(cocktailId)
        }
    }
}