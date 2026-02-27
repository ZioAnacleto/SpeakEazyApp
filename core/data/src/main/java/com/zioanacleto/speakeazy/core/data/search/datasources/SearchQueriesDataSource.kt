package com.zioanacleto.speakeazy.core.data.search.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.search.model.QueryModel

interface SearchQueriesDataSource {
    suspend fun getLastQueries(): Resource<List<QueryModel>>
    suspend fun deleteQuery(query: String)
    suspend fun insertQuery(query: String)
}