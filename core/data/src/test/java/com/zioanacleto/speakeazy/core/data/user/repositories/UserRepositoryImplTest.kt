package com.zioanacleto.speakeazy.core.data.user.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.user.datasources.UserDataSource
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var dataSource: UserDataSource
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getUser() = runBlocking {
        // given
        coEvery { dataSource.getUser() } returns Resource.Success(
            UserModel(name = "testName", email = "testEmail@blablabla.com")
        )

        // when
        val sut = createSut()
        val result = sut.getUser().first()

        // then
        assert(result is Resource.Success<UserModel>)
    }

    @Test
    fun test_saveUser() = runBlocking {
        // given
        val input = UserModel(name = "testName", email = "testEmail@blablabla.com")

        // when
        val sut = createSut()
        sut.saveUser(input)

        // then
        coVerify { dataSource.saveUser(input) }
    }

    @Test
    fun test_updateUser() = runBlocking {
        // given
        val input = UserModel(name = "testName", email = "testEmail@blablabla.com")

        // when
        val sut = createSut()
        sut.updateUser(input)

        // then
        coVerify { dataSource.updateUser(input) }
    }

    @Test
    fun test_deleteUser() = runBlocking {
        // given
        val input = UserModel(name = "testName", email = "testEmail@blablabla.com")

        // when
        val sut = createSut()
        sut.deleteUser(input)

        // then
        coVerify { dataSource.deleteUser(input) }
    }

    private fun createSut() = UserRepositoryImpl(dataSource, dispatcherProvider)
}