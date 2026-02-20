package com.zioanacleto.speakeazy.core.data.main.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel

class MainSpeakEazyBEListDataMapper: DataMapper<MainSpeakEazyBEListResponseDTO, MainModel> {
    override fun mapInto(input: MainSpeakEazyBEListResponseDTO): MainModel {
        return MainModel(
            drinks = input.cocktails.map {
                DrinkModel(
                    id = it.id.default(),
                    name = it.name.default(),
                    category = it.category.default(),
                    imageUrl = it.imageLink.default(),
                    videoUrl = it.videoLink.default(),
                    isAlcoholic = it.isAlcoholic.default(false),
                    type = it.type.default(),
                    method = it.method.toMethod(),
                    username = it.username.default()
                )
            }
        )
    }

    private fun String?.toMethod(): String = this?.replace(" and ", " & ").default()
}