package com.zioanacleto.speakeazy.ui.presentation.user.data.repositories

import com.zioanacleto.speakeazy.ui.presentation.user.domain.UserRepository
import com.zioanacleto.speakeazy.ui.presentation.user.data.datasources.UserDataSource
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val localDataSource: UserDataSource,
    private val dispatcherProvider: DispatcherProvider
): UserRepository {
    override fun getUser(): Flow<Resource<UserModel>> =
        flow {
            emit(
                localDataSource.getUser()
            )
        }.flowOn(dispatcherProvider.io())

    override suspend fun saveUser(user: UserModel) {
        localDataSource.saveUser(user)
    }

    override suspend fun updateUser(user: UserModel) {
        localDataSource.updateUser(user)
    }
}