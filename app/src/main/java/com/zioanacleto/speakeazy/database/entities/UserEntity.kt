package com.zioanacleto.speakeazy.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel

@Entity
data class UserEntity(
    val name: String? = null,
    @PrimaryKey val email: String
)

fun UserEntity.toUserModel() = UserModel(
    name = name.default(),
    email = email
)

fun UserModel.toUserEntity() = UserEntity(
    name = name,
    email = email
)