package com.zioanacleto.speakeazy.ui.presentation.search.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.data.builders.SpeakEazyBEUrlBuilder
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.TagsModel

class NetworkSearchDataSource(
    private val apiClient: ApiClientImpl,
    private val requestDataMapper: DataMapper<String, SearchRequestDTO>,
    private val responseDataMapper: DataMapper<SearchResponseDTO, SearchModel>,
    private val tagsDataMapper: DataMapper<TagsResponseDTO, TagsModel>,
    private val mainDataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>
) : SearchDataSource {
    override suspend fun querySearch(query: String): Resource<SearchModel> {
        return getResponseOrCatchError(responseDataMapper) {
            apiClient.executePostRequest(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Search
                ),
                requestDataMapper.mapInto(query)
            )
        }
    }

    override suspend fun getTags(): Resource<TagsModel> {
        return getResponseOrCatchError(tagsDataMapper) {
            apiClient.executeGetRequest(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Tags
                )
            )
        }
    }

    override suspend fun queryFilter(
        selectedFilterItem: SearchFilterItem,
        filters: List<String>
    ): Resource<MainModel> {
        return getResponseOrCatchError(mainDataMapper) {
            apiClient.executeGetRequest(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Filter(
                        ingredients = if (selectedFilterItem == SearchFilterItem.INGREDIENTS) filters else null,
                        tags = if (selectedFilterItem == SearchFilterItem.TAGS) filters else null
                    )
                )
            )
        }
    }
}