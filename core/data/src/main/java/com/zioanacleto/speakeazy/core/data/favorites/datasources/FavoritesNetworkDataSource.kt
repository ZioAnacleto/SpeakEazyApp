package com.zioanacleto.speakeazy.core.data.favorites.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.core.network.builders.SpeakEazyBEUrlBuilder

class FavoritesNetworkDataSource(
    private val apiClient: ApiClientImpl,
    private val dataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel>
) : FavoritesDataSource {
    override suspend fun getCocktails(): Resource<FavoritesModel> =
        getResponseOrCatchError(dataMapper) {
            apiClient.executeGetRequest<MainSpeakEazyBEListResponseDTO>(
                url = SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Cocktails
                )
            )
        }
}