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
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
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

    // @Test
    fun `sendEmail - email correctly sent`() = runTest {
        // given
        dispatcherProvider = testDispatcher()
        coEvery { userRepository.saveUser(any(), any(), any()) } just runs

        val email = "test@test.com"

        // when
        val sut = createSut()
        sut.sendEmail(email) {}

        // then
    }

    private fun createSut() =
        UserViewModel(userRepository, firebaseAuthRepository, dispatcherProvider)
}
