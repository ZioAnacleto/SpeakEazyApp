package com.zioanacleto.speakeazy.core.domain.search

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun submitQuery(aiSearchMode: Boolean, query: String): Flow<Resource<SearchModel>>
    fun getSearchLandingData(): Flow<Resource<SearchLandingModel>>
    fun submitFilter(
        filters: Map<SearchFilterModel, List<String>>
    ): Flow<Resource<MainModel>>
    suspend fun addQueryToLocalDatabase(query: String)
}