package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.zioanacleto.buffa.base.BaseViewModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.default
import com.zioanacleto.buffa.logging.AnacletoLevel
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.domain.user.FirebaseAuthRepository
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import com.zioanacleto.speakeazy.core.domain.user.model.toLocaleName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: UserRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    private val _onUserSavedError = MutableStateFlow(false)
    val onUserSavedError: StateFlow<Boolean> = _onUserSavedError

    private val _onUserUpdatedError = MutableStateFlow(false)
    val onUserUpdatedError: StateFlow<Boolean> = _onUserUpdatedError

    val userUiState: Flow<UserUiState> =
        userRepository.getUser()
            .mapResourceAsUserUiState()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserUiState.Loading
            )

    fun sendEmail(
        userEmail: String,
        onEmailSent: (Boolean) -> Unit
    ) {
        coroutineScope.launch(dispatcherProvider.io()) {
            userRepository.saveUser(
                UserModel(
                    email = userEmail
                ),
                onSuccess = {
                    AnacletoLogger.mumbling(
                        mumble = "Success in saving local user.",
                        level = AnacletoLevel.INFO
                    )
                    _onUserSavedError.value = false
                },
                onError = { exception ->
                    AnacletoLogger.mumbling(
                        mumble = "Error while saving user",
                        error = exception,
                        level = AnacletoLevel.WARNING
                    )
                    _onUserSavedError.value = true
                }
            )
        }
        // sending email to user
        firebaseAuthRepository.sendSignInLinkToEmail(userEmail, onEmailSent)
    }

    fun updateUserWithName(name: String) {
        // retrieving current user's mail
        val email = firebaseAuthRepository.currentUserEmail.default()

        coroutineScope.launch(dispatcherProvider.io()) {
            userRepository.updateUser(
                UserModel(
                    email = email,
                    name = name
                )
            )
        }
    }

    fun updateUserWithLanguage(userModel: UserModel) {
        coroutineScope.launch(dispatcherProvider.io()) {
            userRepository.updateUser(userModel)
            withContext(dispatcherProvider.main()) {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(userModel.selectedLanguage.toLocaleName())
                )
            }
        }
    }

    fun finishUserLogin(
        emailLink: String,
        email: String,
        onSignInDone: (Boolean) -> Unit
    ) {
        if (firebaseAuthRepository.isSignInWithEmailLink(emailLink)) {
            firebaseAuthRepository.signInWithEmailLink(email, emailLink, onSignInDone)
        } else onSignInDone(false)
    }

    fun logoutUser() {
        coroutineScope.launch(dispatcherProvider.io()) {
            userRepository.deleteUser(
                UserModel(
                    email = firebaseAuthRepository.currentUserEmail.default()
                )
            )
        }

        firebaseAuthRepository.signOut()
    }
}