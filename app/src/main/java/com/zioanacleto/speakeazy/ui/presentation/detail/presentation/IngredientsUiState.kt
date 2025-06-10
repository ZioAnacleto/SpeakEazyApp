package com.zioanacleto.speakeazy.ui.presentation.detail.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface IngredientsUiState {
    data class Success(val ingredients: IngredientsModel) : IngredientsUiState
    data class Error(val exception: Throwable?) : IngredientsUiState
    data object Loading : IngredientsUiState
}

fun Flow<Resource<IngredientsModel>>.mapResourceAsIngredientsUiState() = this.map { ingredientsResource ->
    when(ingredientsResource) {
        is Resource.Success -> IngredientsUiState.Success(ingredientsResource.data)
        is Resource.Error -> IngredientsUiState.Error(ingredientsResource.exception)
        is Resource.Loading -> IngredientsUiState.Loading
    }
}