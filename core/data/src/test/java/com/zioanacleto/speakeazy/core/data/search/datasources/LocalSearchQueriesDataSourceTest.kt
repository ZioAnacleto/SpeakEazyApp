package com.zioanacleto.speakeazy.core.data.search.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.database.dao.SearchDao
import com.zioanacleto.speakeazy.core.database.entities.SearchQueryEntity
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocalSearchQueriesDataSourceTest {
    private lateinit var searchDao: SearchDao
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        searchDao = mockk(relaxed = true)
        performanceTracesManager = mockk(relaxed = true)
        every { performanceTracesManager.startTrace(any(), any()) } just runs
        every { performanceTracesManager.stopTrace(any(), any()) } just runs
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getLastQueries - returns Success when OK`() = runBlocking {
        // given
        every { searchDao.getRecentSearches() } returns listOf()

        // when
        val sut = createSut()
        val response = sut.getLastQueries()

        // then
        assertAllTrue(
            response is Resource.Success,
            (response as Resource.Success).data.isEmpty()
        )
    }

    @Test
    fun `getLastQueries - throws Exception when KO`() = runBlocking {
        // given
        every { searchDao.getRecentSearches() } throws Exception()

        // when
        val sut = createSut()
        val response = sut.getLastQueries()

        // then
        assert(response is Resource.Error)
    }

    @Test
    fun `deleteQuery - should call dao method`() = runBlocking {
        // given
        val query = "testQuery"

        // when
        val sut = createSut()
        sut.deleteQuery(query)

        // then
        verify(exactly = 1) { searchDao.deleteSearch(query) }
    }

    @Test
    fun `insertQuery - should call upsert dao method`() = runBlocking {
        // given
        val query = "testQuery"

        // when
        val sut = createSut()
        sut.insertQuery(query)

        // then
        val slot = slot<SearchQueryEntity>()

        verify(exactly = 1) {
            searchDao.upsertSearch(capture(slot))
        }

        assertEquals(query, slot.captured.name)
        assertTrue(slot.captured.lastUsed > 0)
    }

    private fun createSut() = LocalSearchQueriesDataSource(searchDao, performanceTracesManager)
}