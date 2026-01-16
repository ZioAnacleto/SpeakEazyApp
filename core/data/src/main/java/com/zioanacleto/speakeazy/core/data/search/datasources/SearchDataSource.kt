package com.zioanacleto.speakeazy.core.data.search.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.core.domain.search.model.TagsModel

interface SearchDataSource {
    suspend fun querySearch(query: String): Resource<SearchModel>
    suspend fun getTags(): Resource<TagsModel>
    suspend fun queryFilter(
        filters: Map<SearchFilterModel, List<String>>
    ): Resource<MainModel>
}