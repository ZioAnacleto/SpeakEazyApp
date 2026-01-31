package com.zioanacleto.speakeazy.core.data.search.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.core.data.search.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.core.domain.search.model.TagModel
import com.zioanacleto.speakeazy.core.domain.search.model.TagsModel

class TagsDataMapper : DataMapper<TagsResponseDTO, TagsModel> {
    override fun mapInto(input: TagsResponseDTO): TagsModel {
        return TagsModel(
            tags = input.tags.map {
                TagModel(
                    id = it.id,
                    name = it.name
                )
            }
        )
    }
}