package com.zioanacleto.speakeazy.ui.presentation.create.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

sealed interface CreateCocktailUiState {
    data class SuccessSingle(val createCocktail: CreateCocktailModel) : CreateCocktailUiState
    data class SuccessMultiple(val createCocktails: List<CreateCocktailModel>) : CreateCocktailUiState
    data class Error(val exception: Throwable?) : CreateCocktailUiState
    data object Loading : CreateCocktailUiState
}

fun Flow<Resource<List<CreateCocktailModel>>>.mapResourceAsCreateCocktailUiState() =
    this.map { createCocktailResource ->
        when (createCocktailResource) {
            is Resource.Success -> {
                CreateCocktailUiState.SuccessMultiple(createCocktailResource.data)
            }
            // in this case we create a fake wizard
            is Resource.Error -> CreateCocktailUiState.SuccessSingle(
                CreateCocktailModel(
                    currentStep = CreateWizardStepData.First.order,
                    cocktailName = "",
                    createdTime = Date(),
                    lastUpdateTime = Date(),
                    isAlcoholic = false
                )
            )
            is Resource.Loading -> CreateCocktailUiState.Loading
        }
    }