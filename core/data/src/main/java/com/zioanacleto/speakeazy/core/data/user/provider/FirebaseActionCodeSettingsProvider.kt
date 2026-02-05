package com.zioanacleto.speakeazy.core.data.user.provider

import com.google.firebase.auth.ActionCodeSettings

// implemented in app module
interface FirebaseActionCodeSettingsProvider {
    fun provideActionCodeSettings(): ActionCodeSettings
}