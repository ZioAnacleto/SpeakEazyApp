package com.zioanacleto.speakeazy.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.Language
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.toLanguageFromName

@Entity
data class UserEntity(
    val name: String? = null,
    @PrimaryKey val email: String,
    @ColumnInfo(defaultValue = "ITALIAN")
    val selectedLanguage: String = Language.ITALIAN.name
)

fun UserEntity.toUserModel() = UserModel(
    name = name.default(),
    email = email,
    selectedLanguage = selectedLanguage.toLanguageFromName()
)

fun UserModel.toUserEntity() = UserEntity(
    name = name,
    email = email,
    selectedLanguage = selectedLanguage.name
)