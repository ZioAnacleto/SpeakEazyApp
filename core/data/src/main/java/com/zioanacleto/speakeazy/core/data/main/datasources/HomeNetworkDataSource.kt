package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.core.data.main.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.core.network.builders.SpeakEazyBEUrlBuilder

class HomeNetworkDataSource(
    private val apiClient: ApiClientImpl,
    private val dataMapper: DataMapper<HomeSectionResponseDTO, HomeModel>
) : HomeDataSource {
    override suspend fun getHomeSections(): Resource<HomeModel> {
        return getResponseOrCatchError(dataMapper) {
            apiClient.executeGetRequest<HomeSectionResponseDTO>(
                url = SpeakEazyBEUrlBuilder.buildUrl(SpeakEazyBEUrlBuilder.Endpoint.Home),
                isCached = true
            )
        }
    }
}