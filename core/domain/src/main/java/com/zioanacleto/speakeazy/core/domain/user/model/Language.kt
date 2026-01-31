package com.zioanacleto.speakeazy.core.domain.user.model

enum class Language {
    ITALIAN,
    ENGLISH
}

fun String.toLanguageFromName() = when(this) {
    Language.ITALIAN.name -> Language.ITALIAN
    Language.ENGLISH.name -> Language.ENGLISH
    else -> Language.ITALIAN
}