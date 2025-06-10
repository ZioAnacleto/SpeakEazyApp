package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.viewModelScope
import com.zioanacleto.speakeazy.ui.presentation.main.domain.HomeRepository
import com.zioanacleto.speakeazy.ui.presentation.main.domain.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    repository: MainRepository,
    homeRepository: HomeRepository
): ViewModel() {

    val mainUiState: Flow<MainUiState> =
        repository.getMainList()
            .mapResourceAsMainUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MainUiState.Loading
            )

    val homeUiState: Flow<HomeUiState> =
        homeRepository.getHome()
            .mapResourceAsHomeUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeUiState.Loading
            )
}