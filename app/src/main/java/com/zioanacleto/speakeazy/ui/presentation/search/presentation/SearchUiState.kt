package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface SearchUiState {
    data class Success(val search: SearchModel) : SearchUiState
    data class Error(val exception: Throwable?) : SearchUiState
    data object Loading : SearchUiState
    data object None : SearchUiState
}

fun Flow<Resource<SearchModel>>.mapResourceAsSearchUiState() = this.map { searchResource ->
    when (searchResource) {
        is Resource.Success -> SearchUiState.Success(searchResource.data)
        is Resource.Error -> SearchUiState.Error(searchResource.exception)
        is Resource.Loading -> SearchUiState.Loading
    }
}