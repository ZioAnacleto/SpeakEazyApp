package com.zioanacleto.speakeazy.core.domain.user.model

data class UserModel(
    val name: String = "",
    val email: String,
    val selectedLanguage: Language = Language.ITALIAN
) {
    fun getFirstName() = name.split(" ").first()
}