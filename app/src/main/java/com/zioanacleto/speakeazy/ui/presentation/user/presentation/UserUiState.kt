package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface UserUiState {
    data class Success(val user: UserModel) : UserUiState
    data class Error(val exception: Throwable?) : UserUiState
    data object Loading : UserUiState
}

fun Flow<Resource<UserModel>>.mapResourceAsUserUiState() = this.map { userResource ->
    when(userResource) {
        is Resource.Success -> UserUiState.Success(userResource.data)
        is Resource.Error -> UserUiState.Error(userResource.exception)
        is Resource.Loading -> UserUiState.Loading
    }
}