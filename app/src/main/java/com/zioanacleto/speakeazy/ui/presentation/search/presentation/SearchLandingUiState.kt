package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface SearchLandingUiState {
    data class Success(val data: SearchLandingModel) : SearchLandingUiState
    data class Error(val error: Throwable?) : SearchLandingUiState
    data object Loading : SearchLandingUiState
}

fun Flow<Resource<SearchLandingModel>>.mapResourceAsSearchLandingUiState() =
    this.map { searchResource ->
        when (searchResource) {
            is Resource.Success -> SearchLandingUiState.Success(searchResource.data)
            is Resource.Error -> SearchLandingUiState.Error(searchResource.exception)
            is Resource.Loading -> SearchLandingUiState.Loading
        }
    }