package com.zioanacleto.speakeazy.core.data.search.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.search.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.core.domain.search.model.SearchItem
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel

class SearchResponseDataMapper : DataMapper<SearchResponseDTO, SearchModel> {
    override fun mapInto(input: SearchResponseDTO): SearchModel {
        return SearchModel(
            results = input.cocktails.map { drink ->
                SearchItem.Cocktail(
                    id = drink.id.default(),
                    name = drink.name.default(),
                    imageUrl = drink.imageLink.default(),
                    category = drink.category.default(),
                    favorite = false, // TODO
                    username = drink.username.default()
                )
            } as List<SearchItem>
        )
    }
}