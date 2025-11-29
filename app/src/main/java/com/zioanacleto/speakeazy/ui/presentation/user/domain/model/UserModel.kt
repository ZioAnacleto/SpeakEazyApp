package com.zioanacleto.speakeazy.ui.presentation.user.domain.model

data class UserModel(
    val name: String = "",
    val email: String,
    val selectedLanguage: Language = Language.ITALIAN
) {
    fun getFirstName() = name.split(" ").first()
}