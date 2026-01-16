package com.zioanacleto.speakeazy.core.data.search.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.core.data.search.dto.SearchRequestDTO

class SearchRequestDataMapper : DataMapper<String, SearchRequestDTO> {
    override fun mapInto(input: String): SearchRequestDTO =
        SearchRequestDTO(
            query = input
        )
}