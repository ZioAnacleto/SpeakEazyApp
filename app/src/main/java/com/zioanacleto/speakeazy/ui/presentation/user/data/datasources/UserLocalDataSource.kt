package com.zioanacleto.speakeazy.ui.presentation.user.data.datasources

import android.content.Context
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.logging.AnacletoLevel
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.database.SpeakEazyRoomDatabase
import com.zioanacleto.speakeazy.database.entities.toUserEntity
import com.zioanacleto.speakeazy.database.entities.toUserModel
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel

class UserLocalDataSource(
    context: Context
): UserDataSource {
    private val database: SpeakEazyRoomDatabase = SpeakEazyRoomDatabase.getDatabase(context)

    override suspend fun getUser(): Resource<UserModel> {
        return try {
            val user = database.userDao().getUser().toUserModel()
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

    override suspend fun saveUser(userModel: UserModel) {
        try {
            database.userDao().insertUser(userModel.toUserEntity())

            AnacletoLogger.mumbling(
                mumble = "Success in saving local user.",
                level = AnacletoLevel.INFO
            )
        } catch(exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while saving user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }

    override suspend fun updateUser(userModel: UserModel) {
        try {
            database.userDao().updateUser(userModel.toUserEntity())

            AnacletoLogger.mumbling(
                mumble = "Success in updating local user.",
                level = AnacletoLevel.INFO
            )
        } catch(exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while updating user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }

    override suspend fun deleteUser(userModel: UserModel) {
        try {
            database.userDao().deleteUser(userModel.email)

            AnacletoLogger.mumbling(
                mumble = "Success in deleting local user.",
                level = AnacletoLevel.INFO
            )
        } catch(exception: Exception) {
            AnacletoLogger.mumbling(
                mumble = "Error while deleting user",
                error = exception,
                level = AnacletoLevel.WARNING
            )
        }
    }
}