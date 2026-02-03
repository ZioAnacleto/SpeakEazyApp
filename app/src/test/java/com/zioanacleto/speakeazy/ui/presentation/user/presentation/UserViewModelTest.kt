package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.user.FirebaseAuthRepository
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import com.zioanacleto.speakeazy.testDispatcher
import com.zioanacleto.speakeazy.testResourceFlow
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var firebaseAuthRepository: FirebaseAuthRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true)
        firebaseAuthRepository = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `userUiState - when Resource is Success uiState is Success`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { userRepository.getUser() } returns flowOf(
            Resource.Success(
                UserModel(name = "nameTest", email = "mailTest@blablabla.it")
            )
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.userUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is UserUiState.Loading,
            result is UserUiState.Success
        )
    }

    @Test
    fun `userUiState - when Resource is Error uiState is Error`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { userRepository.getUser() } returns flowOf(
            Resource.Error(Exception("test"))
        )

        // when
        val sut = createSut()
        val (resultLoading, result) = sut.userUiState.testResourceFlow()

        // then
        assertAllTrue(
            resultLoading is UserUiState.Loading,
            result is UserUiState.Error
        )
    }

    @Test
    fun `sendEmail - onSuccess called`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        coEvery {
            userRepository.saveUser(
                any(),
                captureLambda(),
                any()
            )
        } answers {
            lambda<() -> Unit>().invoke()
        }

        val email = "test@test.com"

        // when
        val sut = createSut()
        sut.sendEmail(email) {}

        advanceUntilIdle()

        // then
        assert(!sut.onUserSavedError.value)
    }

    @Test
    fun `sendEmail - onError called`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        coEvery {
            userRepository.saveUser(
                any(),
                any(),
                captureLambda()
            )
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Exception())
        }

        val email = "test@test.com"

        // when
        val sut = createSut()
        sut.sendEmail(email) {}

        advanceUntilIdle()

        // then
        assert(sut.onUserSavedError.value)
    }

    @Test
    fun `sendEmail - email correctly sent`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        coEvery { userRepository.saveUser(any(), any(), any()) } just runs
        val lambda: (Boolean) -> Unit = {}

        val email = "test@test.com"

        // when
        val sut = createSut()
        sut.sendEmail(email, lambda)

        // then
        verify { firebaseAuthRepository.sendSignInLinkToEmail(email, lambda) }
    }

    @Test
    fun `updateUserWithName - should call updateUser`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        val name = "testName"
        val email = "test@test.com"
        every { firebaseAuthRepository.currentUserEmail } returns email

        // when
        val sut = createSut()
        sut.updateUserWithName(name)

        advanceUntilIdle()

        // then
        coVerify { userRepository.updateUser(UserModel(name, email)) }
    }

    @Test
    fun `updateUserWithLanguage - should call updateUser`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        val userModel = UserModel(name = "testName", email = "test@test.com")

        // when
        val sut = createSut()
        sut.updateUserWithLanguage(userModel)

        advanceUntilIdle()

        // then
        coVerify { userRepository.updateUser(userModel) }
    }

    @Test
    fun `finishUserLogin - if isSignInWithEmailLink repository is called`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { firebaseAuthRepository.isSignInWithEmailLink(any()) } returns true
        val emailLink = "testEmailLink"
        val email = "test@test.com"
        val lambda: (Boolean) -> Unit = {}

        // when
        val sut = createSut()
        sut.finishUserLogin(emailLink, email, lambda)

        // then
        verify { firebaseAuthRepository.signInWithEmailLink(email, emailLink, lambda) }
    }

    @Test
    fun `finishUserLogin - if not isSignInWithEmailLink lambda is called`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        every { firebaseAuthRepository.isSignInWithEmailLink(any()) } returns false
        val emailLink = "testEmailLink"
        val email = "test@test.com"
        var isLambdaCalled = false

        // when
        val sut = createSut()
        sut.finishUserLogin(emailLink, email) { isLambdaCalled = true }

        // then
        assert(isLambdaCalled)
    }

    @Test
    fun `logoutUser - signOut method is called`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        coEvery { userRepository.deleteUser(any()) } just runs

        // when
        val sut = createSut()
        sut.logoutUser()

        advanceUntilIdle()

        // then
        verify { firebaseAuthRepository.signOut() }
    }

    private fun createSut() =
        UserViewModel(userRepository, firebaseAuthRepository, dispatcherProvider)
}
