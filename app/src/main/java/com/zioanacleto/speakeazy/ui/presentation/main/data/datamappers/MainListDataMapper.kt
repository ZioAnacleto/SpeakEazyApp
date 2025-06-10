package com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel

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