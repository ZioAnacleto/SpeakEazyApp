package com.zioanacleto.speakeazy.core.data.user.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.UserDao
import com.zioanacleto.speakeazy.core.database.entities.UserEntity
import com.zioanacleto.speakeazy.core.domain.user.model.Language
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserLocalDataSourceTest {
    private lateinit var userDao: UserDao
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        userDao = mockk(relaxed = true)
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
    fun `getUser - should return correct user data`() = runTest {
        // given
        every { userDao.getUser() } returns mockUserEntity()

        // when
        val sut = createSut()
        val response = sut.getUser()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.name == "test name",
            response.data.email == "testmail@gmail.com",
            response.data.selectedLanguage == Language.ITALIAN,
        )
    }

    @Test
    fun `getUser - should return error when Exception is thrown`() = runTest {
        // given
        every { userDao.getUser() } throws Exception()

        // when
        val sut = createSut()
        val response = sut.getUser()

        // then
        assert(response is Resource.Error)
    }

    @Test
    fun `saveUser - user should be saved`() = runTest {
        // given
        every { userDao.insertUser(any()) } just runs
        var onSuccess = false
        var onError = false

        // when
        val sut = createSut()
        sut.saveUser(
            mockUserModel(),
            { onSuccess = true },
            { onError = true }
        )

        // then
        assert(onSuccess)
        assert(!onError)
    }

    @Test
    fun `saveUser - on Exception user should not be saved`() = runTest {
        // given
        every { userDao.insertUser(any()) } throws Exception()
        var onSuccess = false
        var onError = false

        // when
        val sut = createSut()
        sut.saveUser(
            mockUserModel(),
            { onSuccess = true },
            { onError = true }
        )

        // then
        assert(!onSuccess)
        assert(onError)
    }

    private fun createSut() = UserLocalDataSource(userDao, performanceTracesManager)

    private fun mockUserEntity() = UserEntity(
        name = "test name",
        email = "testmail@gmail.com",
        selectedLanguage = Language.ITALIAN.name
    )

    private fun mockUserModel() = UserModel(
        name = "test name",
        email = "testmail@gmail.com",
        selectedLanguage = Language.ITALIAN
    )
}