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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class UserLocalDataSource(
    private val userDao: UserDao,
    private val performanceTracesManager: PerformanceTracesManager
) : UserDataSource {
    override fun getUser(): Flow<Resource<UserModel>> =
        try {
            userDao.getUser().map { userEntity ->
                performanceTracesManager.returningTraceSuspend(
                    UserLocalDataSource::class,
                    "getUser"
                ) {
                    if (userEntity != null) {
                        val user = userEntity.toUserModel()
                        AnacletoLogger.mumbling(
                            mumble = "Success in retrieving local user.",
                            level = AnacletoLevel.INFO
                        )
                        Resource.Success(user)
                    } else {
                        Resource.Error(Exception("User not found"))
                    }
                }
            }.catch { exception ->
                performanceTracesManager.stopTrace(
                    UserLocalDataSource::class,
                    "getUser"
                )
                AnacletoLogger.mumbling(
                    mumble = "Error while converting user",
                    error = exception,
                    level = AnacletoLevel.ERROR
                )
                emit(Resource.Error(exception))
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                UserLocalDataSource::class,
                "getUser"
            )
            AnacletoLogger.mumbling(
                mumble = "Error while retrieving user",
                error = exception,
                level = AnacletoLevel.ERROR
            )
            flowOf(Resource.Error(exception))
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
