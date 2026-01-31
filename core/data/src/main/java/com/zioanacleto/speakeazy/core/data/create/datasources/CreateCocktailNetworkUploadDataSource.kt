package com.zioanacleto.speakeazy.core.data.create.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.core.data.create.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.core.network.builders.SpeakEazyBEUrlBuilder

class CreateCocktailNetworkUploadDataSource(
    private val apiClient: ApiClientImpl,
    private val requestDataMapper: DataMapper<CreateCocktailModel, CreateCocktailRequestDTO>,
) : CreateCocktailUploadDataSource {

    // no need to create a dedicated file for this
    private val responseDataMapper: DataMapper<Int, String> =
        object : DataMapper<Int, String> {
            override fun mapInto(input: Int): String {
                return input.toString()
            }
        }

    override suspend fun uploadCocktail(cocktail: CreateCocktailModel): Resource<String> {
        return getResponseOrCatchError(responseDataMapper) {
            apiClient.executePostRequest(
                url = SpeakEazyBEUrlBuilder.buildUrl(
                    SpeakEazyBEUrlBuilder.Endpoint.CreateCocktail
                ),
                body = requestDataMapper.mapInto(cocktail)
            )
        }
    }
}