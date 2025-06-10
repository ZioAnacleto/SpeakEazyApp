package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel

interface HomeDataSource {
    suspend fun getHomeSections(): Resource<HomeModel>
}