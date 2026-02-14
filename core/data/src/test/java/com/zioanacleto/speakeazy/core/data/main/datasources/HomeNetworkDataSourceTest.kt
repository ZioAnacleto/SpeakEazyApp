package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.createApiClientWithResponse
import com.zioanacleto.speakeazy.core.data.main.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeNetworkDataSourceTest {

    private lateinit var apiClientImpl: ApiClientImpl
    private lateinit var dataMapper: DataMapper<HomeSectionResponseDTO, HomeModel>
    private lateinit var performanceTracesManager: PerformanceTracesManager

    @Before
    fun setUp() {
        dataMapper = mockk(relaxed = true)
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
    fun `getHomeSections - should return correct data`() = runBlocking {
        // given
        apiClientImpl = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = """{"sections": []}"""
        )

        every {
            dataMapper.mapInto(any())
        } returns HomeModel(listOf())

        // when
        val sut = createSut()
        val response = sut.getHomeSections()

        // then
        assert(response is Resource.Success)
        assert((response as Resource.Success).data.sections.isEmpty())
    }

    private fun createSut() = HomeNetworkDataSource(
        apiClientImpl,
        dataMapper,
        performanceTracesManager
    )
}