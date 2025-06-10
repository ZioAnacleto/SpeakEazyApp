package com.zioanacleto.speakeazy.ui.presentation.main.domain

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(): Flow<Resource<HomeModel>>
}