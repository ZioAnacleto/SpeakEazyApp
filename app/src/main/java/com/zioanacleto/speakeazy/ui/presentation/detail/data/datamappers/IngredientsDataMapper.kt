package com.zioanacleto.speakeazy.ui.presentation.detail.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.Ingredient
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel

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