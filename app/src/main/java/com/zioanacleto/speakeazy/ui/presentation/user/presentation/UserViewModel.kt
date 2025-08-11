package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.default
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.ui.presentation.user.domain.UserRepository
import com.zioanacleto.speakeazy.ui.presentation.user.domain.model.UserModel
import com.zioanacleto.speakeazy.ui.presentation.user.navigation.USER_DEEPLINK_URI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {

    private val actionCodeSettings: ActionCodeSettings = actionCodeSettings {
        url = USER_DEEPLINK_URI
        setAndroidPackageName(
            "com.zioanacleto.speakeazy",
            true,
            "24"
        )
        handleCodeInApp = true
    }

    val userUiState: Flow<UserUiState> =
        repository.getUser()
            .mapResourceAsUserUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserUiState.Loading
            )

    fun sendEmail(
        userEmail: String,
        onEmailSent: (Boolean) -> Unit
    ) {
        viewModelScope.launch(dispatcherProvider.io()) {
            repository.saveUser(
                UserModel(
                    email = userEmail
                )
            )
        }
        // sending email to user
        Firebase.auth.sendSignInLinkToEmail(userEmail, actionCodeSettings)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
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

    fun updateUserWithName(name: String) {
        // retrieving current user's mail
        val email = Firebase.auth.currentUser?.email.default()

        viewModelScope.launch(dispatcherProvider.io()) {
            repository.updateUser(
                UserModel(
                    email = email,
                    name = name
                )
            )
        }
    }

    fun finishUserLogin(
        emailLink: String,
        email: String,
        onSignInDone: (Boolean) -> Unit
    ) {
        if (Firebase.auth.isSignInWithEmailLink(emailLink)) {
            Firebase.auth.signInWithEmailLink(email, emailLink)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        AnacletoLogger.mumbling(
                            mumble = "Email sent successfully."
                        )
                    } else {
                        AnacletoLogger.mumbling(
                            mumble = "Email not sent, something went wrong."
                        )
                    }

                    onSignInDone(task.isSuccessful)
                }
        } else onSignInDone(false)
    }

    fun logoutUser() {
        Firebase.auth.signOut()
    }
}