package com.zioanacleto.speakeazy.core.data.detail.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.detail.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.core.domain.detail.model.Ingredient
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel

class IngredientsDataMapper : DataMapper<IngredientsListDTO, IngredientsModel> {
    override fun mapInto(input: IngredientsListDTO): IngredientsModel {
        return IngredientsModel(
            ingredients = input.ingredients?.map {
                Ingredient(
                    id = it.id.default(),
                    name = it.name.default(),
                    imageUrl = it.imageUrl.default()
                )
            } ?: listOf()
        )
    }
}