package com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeSectionModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.IngredientModel

class HomeDataMapper : DataMapper<HomeSectionResponseDTO, HomeModel> {
    override fun mapInto(input: HomeSectionResponseDTO): HomeModel =
        HomeModel(
            sections = input.sections.map { section ->
                HomeSectionModel(
                    id = section.id,
                    name = section.name,
                    cocktails = section.cocktails.map { cocktail ->
                        DrinkModel(
                            id = cocktail.id.default(),
                            name = cocktail.name.default(),
                            category = cocktail.category.default(),
                            instructions = cocktail.instructionsIt.default(),
                            glass = cocktail.glass.default(),
                            isAlcoholic = cocktail.isAlcoholic.default(false),
                            imageUrl = cocktail.imageLink.default(),
                            type = cocktail.type.default(),
                            method = cocktail.method.toMethod(),
                            ingredients = cocktail.ingredients?.ingredients?.map {
                                // todo: add logic to handle cl-oz
                                IngredientModel(
                                    id = it.id.default(),
                                    name = it.name.default(),
                                    imageUrl = it.imageUrl.default(),
                                    measureCl = it.quantityCl.toClMeasure(),
                                    measureOz = it.quantityOz.toOzMeasure(),
                                    measureSpecial = it.quantitySpecial.default()
                                )
                            } ?: listOf(),
                            visualizations = cocktail.visualizations.default()
                        )
                    }
                )
            }
        )

    private fun String?.toMethod(): String = this?.replace(" and ", " & ").default()
    private fun String?.toClMeasure(): String? = this?.let { if(it.contains("-")) null else it.plus("cl") }
    private fun String?.toOzMeasure(): String? = this?.let { if(it.contains("-")) null else it.plus("oz") }
}