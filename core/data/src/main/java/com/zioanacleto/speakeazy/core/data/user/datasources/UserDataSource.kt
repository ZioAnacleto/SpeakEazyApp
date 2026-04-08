package com.zioanacleto.speakeazy.core.data.user.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUser(): Flow<Resource<UserModel>>
    suspend fun saveUser(
        userModel: UserModel,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    )
    suspend fun updateUser(userModel: UserModel)
    suspend fun deleteUser(userModel: UserModel)
}