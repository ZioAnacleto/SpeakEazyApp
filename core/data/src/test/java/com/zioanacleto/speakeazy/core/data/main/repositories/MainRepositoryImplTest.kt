package com.zioanacleto.speakeazy.core.data.main.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.main.datasources.MainDataSource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainRepositoryImplTest {

    private lateinit var networkDataSource: MainDataSource
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
    fun test_getMainList_whenNetworkAndLocalSuccess_returnSuccess() = runBlocking {
        // given
        coEvery { networkDataSource.getMainList() } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )
        coEvery { localDataSource.getMainList() } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )

        // when
        val sut = createSut()
        val response = sut.getMainList().first()

        // then
        assert(response is Resource.Success<MainModel>)
    }

    @Test
    fun test_getMainList_whenNetworkSuccessAndLocalError_returnNetwork() = runBlocking {
        // given
        coEvery { networkDataSource.getMainList() } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )
        coEvery { localDataSource.getMainList() } returns Resource.Error(Exception("test"))

        // when
        val sut = createSut()
        val response = sut.getMainList().first()

        // then
        assertAllTrue(
            response is Resource.Success<MainModel>,
            (response as Resource.Success).data.drinks.isEmpty()
        )
    }

    @Test
    fun test_getMainById_whenNetworkAndLocalSuccess_returnSuccess() = runBlocking {
        // given
        coEvery { networkDataSource.getMainById(any()) } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )
        coEvery { localDataSource.getMainById(any()) } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )

        // when
        val sut = createSut()
        val response = sut.getMainById("1").first()

        // then
        assert(response is Resource.Success<MainModel>)
    }

    @Test
    fun test_getMainById_whenNetworkSuccessAndLocalError_returnNetwork() = runBlocking {
        // given
        coEvery { networkDataSource.getMainById(any()) } returns Resource.Success(
            MainModel(
                drinks = listOf()
            )
        )
        coEvery { localDataSource.getMainById(any()) } returns Resource.Error(Exception("test"))

        // when
        val sut = createSut()
        val response = sut.getMainById("1").first()

        // then
        assertAllTrue(
            response is Resource.Success<MainModel>,
            (response as Resource.Success).data.drinks.isEmpty()
        )
    }

    @Test
    fun test_setFavoriteCocktail() {
        // given
        val cocktailId = "1"
        val cocktailName = "testCocktail"

        // when
        val sut = createSut()
        runBlocking { sut.setFavoriteCocktail(cocktailId, cocktailName) }

        // then
        coVerify { localDataSource.setFavoriteCocktail(cocktailId, cocktailName) }
    }

    @Test
    fun test_deleteFavoriteCocktail() {
        // given
        val cocktailId = "1"

        // when
        val sut = createSut()
        runBlocking { sut.deleteFavoriteCocktail(cocktailId) }

        // then
        coVerify { localDataSource.deleteFavoriteCocktail(cocktailId) }
    }

    @Test
    fun test_updateVisualizations() {
        // given
        val cocktailId = "1"

        // when
        val sut = createSut()
        runBlocking { sut.updateVisualizations(cocktailId) }

        // then
        coVerify { networkDataSource.updateVisualizations(cocktailId) }
    }

    private fun createSut() =
        MainRepositoryImpl(networkDataSource, localDataSource, dispatcherProvider)
}