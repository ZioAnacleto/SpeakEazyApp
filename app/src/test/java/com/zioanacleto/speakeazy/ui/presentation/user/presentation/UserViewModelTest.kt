package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import com.zioanacleto.speakeazy.testResourceFlow
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserViewModelTest {

    private lateinit var repository: UserRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_userUiState_whenResourceIsSuccess_uiStateIsSuccess() = runBlocking {
        // given
        every { repository.getUser() } returns flowOf(
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
    fun test_userUiState_whenResourceIsError_uiStateIsError() = runBlocking {
        // given
        every { repository.getUser() } returns flowOf(
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

    private fun createSut() = UserViewModel(repository, dispatcherProvider)
}