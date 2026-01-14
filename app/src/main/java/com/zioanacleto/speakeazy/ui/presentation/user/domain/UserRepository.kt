package com.zioanacleto.speakeazy.ui.presentation.user.domain

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<Resource<UserModel>>
    suspend fun saveUser(user: UserModel)
    suspend fun updateUser(user: UserModel)
    suspend fun deleteUser(user: UserModel)
}