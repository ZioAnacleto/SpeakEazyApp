package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface MainUiState {
    data class Success(val main: MainModel) : MainUiState
    data class Error(val exception: Throwable?) : MainUiState
    data object Loading : MainUiState
}

fun Flow<Resource<MainModel>>.mapResourceAsMainUiState() = this.map { mainResource ->
    when (mainResource) {
        is Resource.Success -> MainUiState.Success(mainResource.data)
        is Resource.Error -> MainUiState.Error(mainResource.exception)
        is Resource.Loading -> MainUiState.Loading
    }
}