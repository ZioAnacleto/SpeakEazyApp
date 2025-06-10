package com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.data.builders.SpeakEazyBEUrlBuilder
import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel

class IngredientNetworkDataSource(
    private val apiClient: ApiClientImpl,
    private val dataMapper: DataMapper<IngredientsListDTO, IngredientsModel>
) : IngredientDataSource {
    override suspend fun getIngredientsList(): Resource<IngredientsModel> =
        getResponseOrCatchError(dataMapper) {
            apiClient.executeGetRequest<IngredientsListDTO>(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Ingredients
                )
            )
        }

    override suspend fun getIngredientById(id: String): Resource<IngredientsModel> =
        getResponseOrCatchError(dataMapper) {
            apiClient.executeGetRequest<IngredientsListDTO>(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.SingleIngredient(
                        id = id
                    )
                )
            )
        }
}