package com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.TagModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.TagsModel

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