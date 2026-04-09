package com.zioanacleto.speakeazy.core.data.user.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.user.datasources.UserDataSource
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var dataSource: UserDataSource
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        dataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
        performanceTracesManager = mockk(relaxed = true) {
            every { startTrace(any(), any()) } just runs
            every { stopTrace(any(), any()) } just runs
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getUser - when Success should return correct data`() = runTest {
        // given
        coEvery { dataSource.getUser() } returns flowOf(
            Resource.Success(
                UserModel(name = "testName", email = "testEmail@blablabla.com")
            )
        )

        // when
        val sut = createSut()
        val result = sut.getUser().first()

        // then
        assert(result is Resource.Success<UserModel>)
    }

    // TODO: fix this
    // @Test
    fun `saveUser - should call dataSource saveUser method`() = runTest {
        // given
        val input = UserModel(name = "testName", email = "testEmail@blablabla.com")

        // when
        val sut = createSut()
        sut.saveUser(input, {}, {})

        // then
        coVerify { dataSource.saveUser(input, {}, {}) }
    }

    @Test
    fun `updateUser - should call dataSource updateUser method`() = runTest {
        // given
        val input = UserModel(name = "testName", email = "testEmail@blablabla.com")

        // when
        val sut = createSut()
        sut.updateUser(input)

        // then
        coVerify { dataSource.updateUser(input) }
    }

    @Test
    fun `deleteUser - should call dataSource deleteUser method`() = runTest {
        // given
        val input = UserModel(name = "testName", email = "testEmail@blablabla.com")

        // when
        val sut = createSut()
        sut.deleteUser(input)

        // then
        coVerify { dataSource.deleteUser(input) }
    }

    private fun createSut() =
        UserRepositoryImpl(dataSource, dispatcherProvider, performanceTracesManager)
}