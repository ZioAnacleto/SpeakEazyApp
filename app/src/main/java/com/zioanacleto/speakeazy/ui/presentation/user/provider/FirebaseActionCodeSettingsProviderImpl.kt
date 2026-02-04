package com.zioanacleto.speakeazy.ui.presentation.user.provider

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.actionCodeSettings
import com.zioanacleto.speakeazy.APP_PACKAGE
import com.zioanacleto.speakeazy.USER_DEEPLINK_URI
import com.zioanacleto.speakeazy.core.data.user.provider.FirebaseActionCodeSettingsProvider

class FirebaseActionCodeSettingsProviderImpl : FirebaseActionCodeSettingsProvider {
    override fun provideActionCodeSettings(): ActionCodeSettings =
        actionCodeSettings {
            url = USER_DEEPLINK_URI
            setAndroidPackageName(
                APP_PACKAGE,
                true,
                "24"
            )
            handleCodeInApp = true
        }
}