package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.data.builders.SpeakEazyBEUrlBuilder
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO

class FavoritesNetworkDataSource(
    private val apiClient: ApiClientImpl,
    private val dataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel>
): FavoritesDataSource {
    override suspend fun getCocktails(): Resource<FavoritesModel> =
        getResponseOrCatchError(dataMapper) {
            apiClient.executeGetRequest<MainSpeakEazyBEListResponseDTO>(
                url = SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Cocktails
                )
            )
        }
}