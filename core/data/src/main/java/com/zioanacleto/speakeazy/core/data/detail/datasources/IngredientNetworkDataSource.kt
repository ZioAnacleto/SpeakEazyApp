package com.zioanacleto.speakeazy.core.data.detail.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.data.detail.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.core.network.builders.SpeakEazyBEUrlBuilder

class IngredientNetworkDataSource(
    private val apiClient: ApiClientImpl,
    private val dataMapper: DataMapper<IngredientsListDTO, IngredientsModel>,
    private val performanceTracesManager: PerformanceTracesManager
) : IngredientDataSource {
    override suspend fun getIngredientsList(): Resource<IngredientsModel> =
        getResponseOrCatchError(dataMapper) {
            performanceTracesManager.startTrace(
                this::class,
                "getIngredientsList_executeGetRequest"
            )
            val response = apiClient.executeGetRequest(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.Ingredients
                ),
                IngredientsListDTO::class
            )
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientsList_executeGetRequest"
            )

            response
        }

    override suspend fun getIngredientById(id: String): Resource<IngredientsModel> =
        getResponseOrCatchError(dataMapper) {
            performanceTracesManager.startTrace(
                this::class,
                "getIngredientById_executeGetRequest"
            )
            val response = apiClient.executeGetRequest(
                SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.SingleIngredient(
                        id = id
                    )
                ),
                IngredientsListDTO::class
            )
            performanceTracesManager.stopTrace(
                this::class,
                "getIngredientById_executeGetRequest"
            )

            response
        }
}