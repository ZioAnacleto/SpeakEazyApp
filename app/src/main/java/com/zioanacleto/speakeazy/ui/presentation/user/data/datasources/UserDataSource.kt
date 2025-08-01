package com.zioanacleto.speakeazy.ui.presentation.user.data.datasources

import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel
import com.zioanacleto.buffa.events.Resource

interface UserDataSource {
    suspend fun getUser(): Resource<UserModel>
    suspend fun saveUser(userModel: UserModel)
    suspend fun updateUser(userModel: UserModel)
    suspend fun deleteUser(userModel: UserModel)
}