package com.zioanacleto.speakeazy.ui.presentation.search.data.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.TagsModel

interface SearchDataSource {
    suspend fun querySearch(query: String): Resource<SearchModel>
    suspend fun getTags(): Resource<TagsModel>
    suspend fun queryFilter(
        filters: Map<SearchFilterItem, List<String>>
    ): Resource<MainModel>
}