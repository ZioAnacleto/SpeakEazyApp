package com.zioanacleto.speakeazy.ui.presentation.create.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.data.builders.SpeakEazyBEUrlBuilder
import com.zioanacleto.speakeazy.ui.presentation.create.data.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel

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