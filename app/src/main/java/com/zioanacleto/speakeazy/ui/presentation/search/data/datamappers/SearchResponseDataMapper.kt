package com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel

class SearchResponseDataMapper : DataMapper<SearchResponseDTO, SearchModel> {
    override fun mapInto(input: SearchResponseDTO): SearchModel {
        return SearchModel(
            results = input.cocktails.map { drink ->
                SearchItem.Cocktail(
                    id = drink.id.default(),
                    name = drink.name.default(),
                    imageUrl = drink.imageLink.default()
                )
            } as List<SearchItem>
        )
    }
}