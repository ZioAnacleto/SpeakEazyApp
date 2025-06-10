package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.buffa.events.getResponseOrCatchError
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.data.builders.SpeakEazyBEUrlBuilder
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel

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