package com.zioanacleto.speakeazy.ui.presentation.search.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.createApiClientWithResponse
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.TagsModel
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class NetworkSearchDataSourceTest {

    private lateinit var apiClient: ApiClientImpl
    private lateinit var requestDataMapper: DataMapper<String, SearchRequestDTO>
    private lateinit var responseDataMapper: DataMapper<SearchResponseDTO, SearchModel>
    private lateinit var tagsDataMapper: DataMapper<TagsResponseDTO, TagsModel>
    private lateinit var mainDataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>

    @Before
    fun setUp() {
        requestDataMapper = mockk(relaxed = true)
        responseDataMapper = mockk(relaxed = true)
        tagsDataMapper = mockk(relaxed = true)
        mainDataMapper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_querySearch_whenResponseOK_returnSuccess() = runBlocking {
        // given
        apiClient = createApiClientWithResponse(
            status = HttpStatusCode.OK,
            response = """{"cocktails": []}"""
        )
        val query = "test"
        every { requestDataMapper.mapInto(any()) } returns SearchRequestDTO(query)
        every { responseDataMapper.mapInto(any()) } returns SearchModel()

        // when
        val sut = createSut()
        val result = sut.querySearch(query)

        // then
        assertAllTrue(
            result is Resource.Success,
            (result as Resource.Success).data.results.isEmpty()
        )
    }

    private fun createSut() = NetworkSearchDataSource(
        apiClient,
        requestDataMapper,
        responseDataMapper,
        tagsDataMapper,
        mainDataMapper
    )
}