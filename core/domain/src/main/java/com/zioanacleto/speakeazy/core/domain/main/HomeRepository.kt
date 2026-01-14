package com.zioanacleto.speakeazy.core.domain.main

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(): Flow<Resource<HomeModel>>
}