package com.zioanacleto.speakeazy.core.domain.user

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<Resource<UserModel>>
    suspend fun saveUser(user: UserModel)
    suspend fun updateUser(user: UserModel)
    suspend fun deleteUser(user: UserModel)
}