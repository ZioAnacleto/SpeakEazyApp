package com.zioanacleto.speakeazy.ui.presentation.search.domain

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchLandingModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun submitQuery(query: String): Flow<Resource<SearchModel>>
    fun getSearchLandingData(): Flow<Resource<SearchLandingModel>>
    fun submitFilter(
        selectedFilters: Map<SearchFilterItem, List<String>>
    ): Flow<Resource<MainModel>>
}