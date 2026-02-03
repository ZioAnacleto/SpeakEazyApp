package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl.Companion.CACHE_MAX_AGE_FIVE_MINUTE
import com.zioanacleto.speakeazy.core.network.builders.SpeakEazyBEUrlBuilder

class MainNetworkDataSource(
    private val apiClient: ApiClientImpl,
    private val dataMapper: DataMapper<MainSpeakEazyBEResponseDTO, MainModel>,
    private val listDataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>
) : MainDataSource {
    override suspend fun getMainList(): Resource<MainModel> =
        getResponseOrCatchError(listDataMapper) {
            apiClient.executeGetRequest<MainSpeakEazyBEListResponseDTO>(
                url = SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Cocktails
                )
            )
        }

    override suspend fun getMainById(id: String): Resource<MainModel> =
        getResponseOrCatchError(dataMapper) {
            apiClient.executeGetRequest<MainSpeakEazyBEResponseDTO>(
                url = SpeakEazyBEUrlBuilder.buildUrl(
                    endpoint = SpeakEazyBEUrlBuilder.Endpoint.SingleCocktail(
                        id = id
                    )
                ),
                isCached = true,
                maxAgeSeconds = CACHE_MAX_AGE_FIVE_MINUTE
            )
        }

    override suspend fun setFavoriteCocktail(
        cocktailId: String,
        cocktailName: String
    ) { /* not to be implemented here */ }

    override suspend fun deleteFavoriteCocktail(
        cocktailId: String
    ) { /* not to be implemented here */ }

    override suspend fun updateVisualizations(
        cocktailId: String
    ) {
        apiClient.executePutRequest<Int>(
            url = SpeakEazyBEUrlBuilder.buildUrl(
                endpoint = SpeakEazyBEUrlBuilder.Endpoint.SingleCocktail(
                    id = cocktailId
                )
            )
        )
    }
}