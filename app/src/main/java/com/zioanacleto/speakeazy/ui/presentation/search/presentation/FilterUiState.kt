package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface FilterUiState {
    data class Success(val filter: MainModel) : FilterUiState
    data class Error(val exception: Throwable?) : FilterUiState
    data object Loading : FilterUiState
    data object None : FilterUiState
}

fun Flow<Resource<MainModel>>.mapResourceAsFilterUiState() = this.map { mainResource ->
    when (mainResource) {
        is Resource.Success -> FilterUiState.Success(mainResource.data)
        is Resource.Error -> FilterUiState.Error(mainResource.exception)
        is Resource.Loading -> FilterUiState.Loading
    }
}