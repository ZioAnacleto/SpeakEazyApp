package com.zioanacleto.speakeazy.core.data.search.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.returningTraceSuspend
import com.zioanacleto.speakeazy.core.analytics.traces.trace
import com.zioanacleto.speakeazy.core.database.dao.SearchDao
import com.zioanacleto.speakeazy.core.database.entities.SearchQueryEntity
import com.zioanacleto.speakeazy.core.database.entities.toModel
import com.zioanacleto.speakeazy.core.domain.search.model.QueryModel

class LocalSearchQueriesDataSource(
    private val searchDao: SearchDao,
    private val performanceTracesManager: PerformanceTracesManager
) : SearchQueriesDataSource {
    override suspend fun getLastQueries(): Resource<List<QueryModel>> {
        return try {
            performanceTracesManager.returningTraceSuspend(
                this::class,
                "getLastQueries"
            ) {
                val queries = searchDao.getRecentSearches().map { it.toModel() }
                Resource.Success(queries)
            }
        } catch (exception: Exception) {
            performanceTracesManager.stopTrace(
                this::class,
                "getLastQueries"
            )
            Resource.Error(exception)
        }
    }

    override suspend fun deleteQuery(query: String) =
        performanceTracesManager.trace(
            this::class,
            "deleteQuery"
        ) {
            // delete query from database
            searchDao.deleteSearch(query)
        }

    override suspend fun insertQuery(query: String) =
        performanceTracesManager.trace(
            this::class,
            "deleteQuery"
        ) {
            // insert/update query in database, saving last used time as long
            searchDao.upsertSearch(
                SearchQueryEntity(
                    name = query,
                    lastUsed = System.currentTimeMillis()
                )
            )
        }
}