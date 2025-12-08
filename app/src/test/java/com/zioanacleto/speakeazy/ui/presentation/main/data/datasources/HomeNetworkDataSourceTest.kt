package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.createApiClientWithResponse
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeNetworkDataSourceTest {

    private lateinit var apiClientImpl: ApiClientImpl
    private lateinit var dataMapper: DataMapper<HomeSectionResponseDTO, HomeModel>

    @Before
    fun setUp() {
        dataMapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_getHomeSections() = runBlocking{
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
        dataMapper
    )
}