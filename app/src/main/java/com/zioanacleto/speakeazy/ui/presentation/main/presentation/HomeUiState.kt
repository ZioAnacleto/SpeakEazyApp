package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface HomeUiState {
    data class Success(val home: HomeModel) : HomeUiState
    data class Error(val exception: Throwable?) : HomeUiState
    data object Loading : HomeUiState
}

fun Flow<Resource<HomeModel>>.mapResourceAsHomeUiState() = this.map { homeResource ->
    when (homeResource) {
        is Resource.Success -> HomeUiState.Success(homeResource.data)
        is Resource.Error -> HomeUiState.Error(homeResource.exception)
        is Resource.Loading -> HomeUiState.Loading
    }
}