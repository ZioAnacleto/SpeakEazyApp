package com.zioanacleto.speakeazy.core.data.user.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.logging.AnacletoLevel
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.returningTraceSuspend
import com.zioanacleto.speakeazy.core.analytics.traces.traceSuspend
import com.zioanacleto.speakeazy.core.database.dao.UserDao
import com.zioanacleto.speakeazy.core.database.entities.toUserEntity
import com.zioanacleto.speakeazy.core.database.entities.toUserModel
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel

class UserLocalDataSource(
    private val userDao: UserDao,
    private val performanceTracesManager: PerformanceTracesManager
) : UserDataSource {
    override suspend fun getUser(): Resource<UserModel> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getUser"
            ) {
                val user = userDao.getUser().toUserModel()
                AnacletoLogger.mumbling(
                    mumble = "Success in retrieving local user.",
                    level = AnacletoLevel.INFO
                )

                Resource.Success(user)
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getUser"
            )
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
            performanceTracesManager.traceSuspend(
                this::class,
                "saveUser"
            ) {
                userDao.insertUser(userModel.toUserEntity())
                onSuccess()
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "saveUser"
            )
            onError(exception)
        }
    }

    override suspend fun updateUser(userModel: UserModel) {
        try {
            performanceTracesManager.traceSuspend(
                this::class,
                "updateUser"
            ) {
                userDao.updateUser(userModel.toUserEntity())

                AnacletoLogger.mumbling(
                    mumble = "Success in updating local user.",
                    level = AnacletoLevel.INFO
                )
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "updateUser"
            )
            AnacletoLogger.mumbling(
                mumble = "Error while updating user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }

    override suspend fun deleteUser(userModel: UserModel) {
        try {
            performanceTracesManager.traceSuspend(
                this::class,
                "deleteUser"
            ) {
                userDao.deleteUser(userModel.email)

                AnacletoLogger.mumbling(
                    mumble = "Success in deleting local user.",
                    level = AnacletoLevel.INFO
                )
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "deleteUser"
            )
            AnacletoLogger.mumbling(
                mumble = "Error while deleting user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }
}