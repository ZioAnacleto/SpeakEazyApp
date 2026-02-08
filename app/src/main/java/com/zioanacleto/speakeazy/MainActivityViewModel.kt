package com.zioanacleto.speakeazy

import com.zioanacleto.buffa.base.BaseViewModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserUiState
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.mapResourceAsUserUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(
    repository: UserRepository,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    val userUiState: StateFlow<UserUiState> =
        repository.getUser()
            .mapResourceAsUserUiState()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserUiState.Loading
            )
}