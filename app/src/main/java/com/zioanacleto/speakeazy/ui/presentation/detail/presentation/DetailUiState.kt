package com.zioanacleto.speakeazy.ui.presentation.detail.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface DetailUiState {
    data class Success(val detail: MainModel) : DetailUiState
    data class Error(val exception: Throwable?) : DetailUiState
    data object Loading : DetailUiState
}

fun Flow<Resource<MainModel>>.mapResourceAsDetailUiState() = this.map { detailResource ->
    when(detailResource) {
        is Resource.Success -> DetailUiState.Success(detailResource.data)
        is Resource.Error -> DetailUiState.Error(detailResource.exception)
        is Resource.Loading -> DetailUiState.Loading
    }
}