package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import com.zioanacleto.buffa.base.BaseViewModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.core.domain.main.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    homeRepository: HomeRepository,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    val homeUiState: Flow<HomeUiState> =
        homeRepository.getHome()
            .mapResourceAsHomeUiState()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeUiState.Loading
            )
}