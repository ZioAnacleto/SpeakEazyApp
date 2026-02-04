package com.zioanacleto.speakeazy.core.data.user.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.logging.AnacletoLevel
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.database.dao.UserDao
import com.zioanacleto.speakeazy.core.database.entities.toUserEntity
import com.zioanacleto.speakeazy.core.database.entities.toUserModel
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel

class UserLocalDataSource(
    private val userDao: UserDao
) : UserDataSource {
    override suspend fun getUser(): Resource<UserModel> {
        return try {
            val user = userDao.getUser().toUserModel()
            AnacletoLogger.mumbling(
                mumble = "Success in retrieving local user.",
                level = AnacletoLevel.INFO
            )

            Resource.Success(user)
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while retrieving user",
                error = exception,
                level = AnacletoLevel.ERROR
            )
            Resource.Error(exception)
        }
    }

    override suspend fun saveUser(
        userModel: UserModel,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            userDao.insertUser(userModel.toUserEntity())
            onSuccess()
        } catch (exception: Exception) {
            onError(exception)
        }
    }

    override suspend fun updateUser(userModel: UserModel) {
        try {
            userDao.updateUser(userModel.toUserEntity())

            AnacletoLogger.mumbling(
                mumble = "Success in updating local user.",
                level = AnacletoLevel.INFO
            )
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while updating user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }

    override suspend fun deleteUser(userModel: UserModel) {
        try {
            userDao.deleteUser(userModel.email)

            AnacletoLogger.mumbling(
                mumble = "Success in deleting local user.",
                level = AnacletoLevel.INFO
            )
        } catch (exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while deleting user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }
}