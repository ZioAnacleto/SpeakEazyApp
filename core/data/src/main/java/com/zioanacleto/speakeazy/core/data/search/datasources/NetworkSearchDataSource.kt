package com.zioanacleto.speakeazy.core.data.search.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.returningTraceSuspend
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.data.search.dto.SearchRequestDTO
import com.zioanacleto.speakeazy.core.data.search.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.core.data.search.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.core.domain.search.model.TagsModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.core.network.builders.SpeakEazyBEUrlBuilder
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
class NetworkSearchDataSource(
    private val apiClient: ApiClientImpl,
    private val requestDataMapper: DataMapper<String, SearchRequestDTO>,
    private val responseDataMapper: DataMapper<SearchResponseDTO, SearchModel>,
    private val tagsDataMapper: DataMapper<TagsResponseDTO, TagsModel>,
    private val mainDataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>,
    private val performanceTracesManager: PerformanceTracesManager
) : SearchDataSource {
    override suspend fun querySearch(isAiSearchMode: Boolean, query: String): Resource<SearchModel> {
        return performanceTracesManager.returningTraceSuspend(
            this::class,
            "querySearch"
        ) {
            getResponseOrCatchError(responseDataMapper) {
                if(isAiSearchMode) {
                    apiClient.executePostRequest(
                        url = SpeakEazyBEUrlBuilder.buildUrl(
                            SpeakEazyBEUrlBuilder.Endpoint.Search()
                        ),
                        body = requestDataMapper.mapInto(query),
                        bodySerializer = SearchRequestDTO.serializer(),
                        responseSerializer = SearchResponseDTO.serializer()
                    )
                } else {
                    apiClient.executeGetRequest(
                        url = SpeakEazyBEUrlBuilder.buildUrl(
                            SpeakEazyBEUrlBuilder.Endpoint.Search(query)
                        ),
                        responseType = SearchResponseDTO::class
                    )
                }
            }
        }
    }

    override suspend fun getTags(): Resource<TagsModel> {
        return performanceTracesManager.returningTraceSuspend(
            this::class,
            "getTags"
        ) {
            getResponseOrCatchError(tagsDataMapper) {
                apiClient.executeGetRequest(
                    SpeakEazyBEUrlBuilder.buildUrl(
                        SpeakEazyBEUrlBuilder.Endpoint.Tags
                    ),
                    TagsResponseDTO::class
                )
            }
        }
    }

    override suspend fun queryFilter(
        filters: Map<SearchFilterModel, List<String>>
    ): Resource<MainModel> {
        return performanceTracesManager.returningTraceSuspend(
            this::class,
            "queryFilter"
        ) {
            getResponseOrCatchError(mainDataMapper) {
                apiClient.executeGetRequest(
                    SpeakEazyBEUrlBuilder.buildUrl(
                        SpeakEazyBEUrlBuilder.Endpoint.Filter(
                            ingredients = filters[SearchFilterModel.INGREDIENTS],
                            tags = filters[SearchFilterModel.TAGS]
                        )
                    ),
                    MainSpeakEazyBEListResponseDTO::class
                )
            }
        }
    }
}