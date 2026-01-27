package com.zioanacleto.speakeazy.core.domain.user

interface FirebaseAuthRepository {
    val currentUserEmail: String?
    fun sendSignInLinkToEmail(email: String, onEmailSent: (Boolean) -> Unit)
    fun isSignInWithEmailLink(emailLink: String): Boolean
    fun signInWithEmailLink(email: String, emailLink: String, onSignInDone: (Boolean) -> Unit)
    fun signOut()
}