package com.zioanacleto.speakeazy.core.data.user.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.user.datasources.UserDataSource
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val localDataSource: UserDataSource,
    private val dispatcherProvider: DispatcherProvider
) : UserRepository {
    override fun getUser(): Flow<Resource<UserModel>> =
        flow {
            emit(
                localDataSource.getUser()
            )
        }.flowOn(dispatcherProvider.io())

    override suspend fun saveUser(
        user: UserModel,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) = localDataSource.saveUser(user, onSuccess, onError)

    override suspend fun updateUser(user: UserModel) {
        localDataSource.updateUser(user)
    }

    override suspend fun deleteUser(user: UserModel) {
        localDataSource.deleteUser(user)
    }
}