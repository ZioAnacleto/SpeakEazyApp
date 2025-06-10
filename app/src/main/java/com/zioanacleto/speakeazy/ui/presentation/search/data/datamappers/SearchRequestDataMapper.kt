package com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchRequestDTO

class SearchRequestDataMapper : DataMapper<String, SearchRequestDTO> {
    override fun mapInto(input: String): SearchRequestDTO =
        SearchRequestDTO(
            query = input
        )
}