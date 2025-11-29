package com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.IngredientModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel

class MainSpeakEazyBEDataMapper: DataMapper<MainSpeakEazyBEResponseDTO, MainModel> {
    override fun mapInto(input: MainSpeakEazyBEResponseDTO): MainModel {
        return MainModel(
            listOf(
                DrinkModel(
                    id = input.id.default(),
                    name = input.name.default(),
                    category = input.category.default(),
                    instructions = input.instructions.default(),
                    instructionsIt = input.instructionsIt.default(),
                    glass = input.glass.default(),
                    isAlcoholic = input.isAlcoholic.default(false),
                    imageUrl = input.imageLink.default(),
                    type = input.type.default(),
                    method = input.method.toMethod(),
                    ingredients = input.ingredients?.ingredients?.map {
                        IngredientModel(
                            id = it.id.default(),
                            name = it.name.default(),
                            imageUrl = it.imageUrl.default(),
                            measureCl = it.quantityCl.toClMeasure(),
                            measureOz = it.quantityOz.toOzMeasure(),
                            measureSpecial = it.quantitySpecial.default()
                        )
                    } ?: listOf(),
                    visualizations = input.visualizations.default(),
                    username = input.username.default(),
                    userId = input.userId.default()
                )
            )
        )
    }

    private fun String?.toMethod(): String = this?.replace(" and ", " & ").default()
    private fun String?.toClMeasure(): String? = this?.let { if(it.contains("-")) null else it.plus("cl") }
    private fun String?.toOzMeasure(): String? = this?.let { if(it.contains("-")) null else it.plus("oz") }
}