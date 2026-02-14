package com.zioanacleto.speakeazy.core.data.detail.repositories

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class DetailRepositoryImplTest {

    private lateinit var dataSource: IngredientDataSource
    private lateinit var performanceTracesManager: PerformanceTracesManager
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dataSource = mockk(relaxed = true)
        performanceTracesManager = mockk(relaxed = true)
        every { performanceTracesManager.startTrace(any(), any()) } just runs
        every { performanceTracesManager.stopTrace(any(), any()) } just runs
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getIngredientsList() = runBlocking {
        // given
        coEvery { dataSource.getIngredientsList() } returns mockk(relaxed = true)
        val sut = createSut()

        // when
        val result = sut.getIngredientsList().first()

        // then
        assertNotNull(result)
    }

    @Test
    fun test_getIngredientById() = runBlocking {
        // given
        coEvery { dataSource.getIngredientById(any()) } returns mockk(relaxed = true)
        val sut = createSut()

        // when
        val result = sut.getIngredientById("1").first()

        // then
        assertNotNull(result)
    }

    private fun createSut() = DetailRepositoryImpl(
        dataSource = dataSource,
        dispatcherProvider = dispatcherProvider,
        performanceTracesManager = performanceTracesManager
    )
}