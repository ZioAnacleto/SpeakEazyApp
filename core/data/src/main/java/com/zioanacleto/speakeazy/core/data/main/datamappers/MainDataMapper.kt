package com.zioanacleto.speakeazy.core.data.main.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.main.dto.MainResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.core.domain.main.model.IngredientModel
import com.zioanacleto.speakeazy.core.domain.main.model.InstructionModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel

class MainDataMapper : DataMapper<MainResponseDTO, MainModel> {
    override fun mapInto(input: MainResponseDTO): MainModel = MainModel(
        drinks = input.drinks?.mapNotNull {
            DrinkModel(
                id = it?.idDrink.default(),
                name = it?.strDrink.default(),
                category = it?.strCategory.default(),
                instructions = listOf(
                    InstructionModel(
                        type = "ingredient",
                        instruction = it?.strInstructionsIT.default()
                    )
                ),
                glass = it?.strGlass.default(),
                imageUrl = it?.strImageSource.default(),
                ingredients = it?.gatherAllIngredients()?.map { (ingredient, measure) ->
                    IngredientModel(
                        id = "",
                        name = ingredient,
                        imageUrl = "",
                        measureCl = measure,
                        measureOz = measure
                    )
                } ?: listOf()
            )
        }?.toList() ?: listOf()
    )

    private fun MainResponseDTO.DrinkDTO.gatherAllIngredients(): Map<String, String> {
        val ingredients = mutableMapOf<String, String>()

        with(this) {
            if (
                !strIngredient1.isNullOrBlank() &&
                !strMeasure1.isNullOrBlank()
            ) ingredients[strIngredient1] = strMeasure1
            if (
                !strIngredient2.isNullOrBlank() &&
                !strMeasure2.isNullOrBlank()
            ) ingredients[strIngredient2] = strMeasure2
            if (
                !strIngredient3.isNullOrBlank() &&
                !strMeasure3.isNullOrBlank()
            ) ingredients[strIngredient3] = strMeasure3
            if (
                !strIngredient4.isNullOrBlank() &&
                !strMeasure4.isNullOrBlank()
            ) ingredients[strIngredient4] = strMeasure4
            if (
                !strIngredient5.isNullOrBlank() &&
                !strMeasure5.isNullOrBlank()
            ) ingredients[strIngredient5] = strMeasure5
            if (
                !strIngredient6.isNullOrBlank() &&
                !strMeasure6.isNullOrBlank()
            ) ingredients[strIngredient6] = strMeasure6
            if (
                !strIngredient7.isNullOrBlank() &&
                !strMeasure7.isNullOrBlank()
            ) ingredients[strIngredient7] = strMeasure7
            if (
                !strIngredient8.isNullOrBlank() &&
                !strMeasure8.isNullOrBlank()
            ) ingredients[strIngredient8] = strMeasure8
            if (
                !strIngredient9.isNullOrBlank() &&
                !strMeasure9.isNullOrBlank()
            ) ingredients[strIngredient9] = strMeasure9
            if (
                !strIngredient10.isNullOrBlank() &&
                !strMeasure10.isNullOrBlank()
            ) ingredients[strIngredient10] = strMeasure10
        }

        return ingredients
    }
}