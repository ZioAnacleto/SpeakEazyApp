package com.zioanacleto.speakeazy.core.data.user.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.data.user.provider.FirebaseActionCodeSettingsProvider
import com.zioanacleto.speakeazy.core.domain.user.FirebaseAuthRepository

class FirebaseAuthRepositoryImpl(
    private val actionCodeSettingsProvider: FirebaseActionCodeSettingsProvider
) : FirebaseAuthRepository {
    private val actionCodeSettings by lazy {
        actionCodeSettingsProvider.provideActionCodeSettings()
    }

    override val currentUserEmail: String? = Firebase.auth.currentUser?.email
    override val currentUserId: String? = Firebase.auth.currentUser?.uid

    override fun sendSignInLinkToEmail(
        email: String,
        onEmailSent: (Boolean) -> Unit
    ) {
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AnacletoLogger.mumbling(
                        mumble = "Email sent successfully."
                    )
                    onEmailSent(true)
                } else {
                    AnacletoLogger.mumbling(
                        mumble = "Email not sent, something went wrong."
                    )
                    onEmailSent(false)
                }
            }
    }

    override fun isSignInWithEmailLink(emailLink: String): Boolean =
        Firebase.auth.isSignInWithEmailLink(emailLink)

    override fun signInWithEmailLink(
        email: String,
        emailLink: String,
        onSignInDone: (Boolean) -> Unit
    ) {
        Firebase.auth.signInWithEmailLink(email, emailLink)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AnacletoLogger.mumbling(
                        mumble = "Email verified successfully."
                    )
                } else {
                    AnacletoLogger.mumbling(
                        mumble = "Email not verified, something went wrong."
                    )
                }

                onSignInDone(task.isSuccessful)
            }
    }

    override fun signOut() = Firebase.auth.signOut()
}