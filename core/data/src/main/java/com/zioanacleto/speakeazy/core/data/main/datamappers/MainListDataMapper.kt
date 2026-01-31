package com.zioanacleto.speakeazy.core.data.main.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.main.dto.MainListResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel

class MainListDataMapper: DataMapper<MainListResponseDTO, MainModel> {
    override fun mapInto(input: MainListResponseDTO): MainModel =
        MainModel(
            drinks = input.drinks?.mapNotNull {
                DrinkModel(
                    id = it?.idDrink.default(),
                    name = it?.strDrink.default(),
                    imageUrl = it?.strDrinkThumb.default()
                )
            } ?: arrayListOf()
        )
}