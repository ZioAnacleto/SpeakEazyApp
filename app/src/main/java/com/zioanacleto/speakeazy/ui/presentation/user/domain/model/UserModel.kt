package com.zioanacleto.speakeazy.ui.presentation.user.domain.model

data class UserModel(
    val name: String = "",
    val email: String
) {
    fun getFirstName() = name.split(" ").first()
}