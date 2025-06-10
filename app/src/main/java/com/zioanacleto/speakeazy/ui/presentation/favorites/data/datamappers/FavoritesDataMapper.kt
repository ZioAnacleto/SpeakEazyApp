package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel

class FavoritesDataMapper : DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel> {
    override fun mapInto(input: MainSpeakEazyBEListResponseDTO): FavoritesModel =
        FavoritesModel(
            favorites = input.cocktails.map {
                DrinkModel(
                    id = it.id.default(),
                    name = it.name.default(),
                    category = it.category.default(),
                    imageUrl = it.imageLink.default(),
                    isAlcoholic = it.isAlcoholic.default(false),
                    type = it.type.default(),
                    method = it.method.toMethod()
                )
            }
        )

    private fun String?.toMethod(): String = this?.replace(" and ", " & ").default()
}