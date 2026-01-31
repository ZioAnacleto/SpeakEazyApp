package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel

interface HomeDataSource {
    suspend fun getHomeSections(): Resource<HomeModel>
}