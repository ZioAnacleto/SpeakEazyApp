package com.zioanacleto.speakeazy.ui.presentation.main.data.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.HomeDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.MainDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeSectionModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeRepositoryImplTest {

    private lateinit var networkDataSource: HomeDataSource
    private lateinit var localDataSource: MainDataSource
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        networkDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getHome_whenBothAreSuccess_returnSuccess() = runBlocking {
        // given
        coEvery { networkDataSource.getHomeSections() } returns Resource.Success(
            HomeModel(
                sections = listOf()
            )
        )
        coEvery { localDataSource.getMainList() } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )

        // when
        val sut = createSut()
        val response = sut.getHome().first()

        // then
        assert(response is Resource.Success)
    }

    @Test
    fun test_getHome_whenLocalErrorAndNetworkSuccess_returnNetwork() = runBlocking {
        // given
        coEvery { networkDataSource.getHomeSections() } returns Resource.Success(
            HomeModel(
                sections = listOf(
                    HomeSectionModel(
                        id = "1",
                        name = "test",
                        cocktails = listOf()
                    )
                )
            )
        )
        coEvery { localDataSource.getMainList() } returns Resource.Error(Exception("test"))

        // when
        val sut = createSut()
        val response = sut.getHome().first()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.sections.isNotEmpty()
        )
    }

    private fun createSut() = HomeRepositoryImpl(networkDataSource, localDataSource, dispatcherProvider)
}