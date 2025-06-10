package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.data.builders.SpeakEazyBEUrlBuilder
import com.zioanacleto.speakeazy.data.builders.TheCocktailDBUrlBuilder
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel

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
                )
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