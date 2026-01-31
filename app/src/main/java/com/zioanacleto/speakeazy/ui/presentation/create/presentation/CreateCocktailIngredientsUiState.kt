package com.zioanacleto.speakeazy.ui.presentation.create.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface CreateCocktailIngredientsUiState {
    data class Success(val ingredients: IngredientsModel) : CreateCocktailIngredientsUiState
    data class Error(val error: Throwable) : CreateCocktailIngredientsUiState
    data object Loading : CreateCocktailIngredientsUiState
}

fun Flow<Resource<IngredientsModel>>.mapResourceAsCreateCocktailIngredientsUiState() = this.map {
    when (it) {
        is Resource.Success -> CreateCocktailIngredientsUiState.Success(it.data)
        is Resource.Error -> CreateCocktailIngredientsUiState.Error(it.exception)
        is Resource.Loading -> CreateCocktailIngredientsUiState.Loading
    }
}