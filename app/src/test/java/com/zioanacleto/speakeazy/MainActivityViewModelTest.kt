package com.zioanacleto.speakeazy

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserUiState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityViewModelTest {
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
    fun `userUiState - when success return Success`() = runTest {
        // given
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        every { repository.getUser() } returns flowOf(
            Resource.Success(
                UserModel(
                    name = "testName",
                    email = "testEmail"
                )
            )
        )

        // when
        val sut = createSut()
        val (responseLoading, responseSuccess) = sut.userUiState.testResourceFlow()

        // then
        assertAllTrue(
            responseLoading is UserUiState.Loading,
            responseSuccess is UserUiState.Success
        )
    }

    @Test
    fun `userUiState - when error return Error`() = runTest {
        // given
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        every { repository.getUser() } returns flowOf(
            Resource.Error(Exception("testException"))
        )

        // when
        val sut = createSut()
        val (responseLoading, responseSuccess) = sut.userUiState.testResourceFlow()

        // then
        assertAllTrue(
            responseLoading is UserUiState.Loading,
            responseSuccess is UserUiState.Error
        )
    }

    private fun createSut() = MainActivityViewModel(repository, dispatcherProvider)
}