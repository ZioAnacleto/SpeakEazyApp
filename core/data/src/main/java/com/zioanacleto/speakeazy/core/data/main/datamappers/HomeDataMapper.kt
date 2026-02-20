package com.zioanacleto.speakeazy.core.data.main.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.main.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEInstructionDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.core.domain.main.model.BannerModel
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import com.zioanacleto.speakeazy.core.domain.main.model.HomeSectionModel
import com.zioanacleto.speakeazy.core.domain.main.model.IngredientModel
import com.zioanacleto.speakeazy.core.domain.main.model.InstructionModel

class HomeDataMapper : DataMapper<HomeSectionResponseDTO, HomeModel> {
    override fun mapInto(input: HomeSectionResponseDTO): HomeModel =
        HomeModel(
            sections = input.sections.map { section ->
                HomeSectionModel(
                    id = section.id,
                    name = section.name,
                    cocktails = section.cocktails.map { cocktail ->
                        cocktail.toDrinkModel()
                    }
                )
            },
            banner = input.banner?.let {
                BannerModel(
                    name = it.name.default(),
                    position = it.position.default(),
                    cta = it.cta.default(),
                    cocktail = it.cocktailInfo.toDrinkModel()
                )
            }
        )

    private fun MainSpeakEazyBEResponseDTO.toDrinkModel() = DrinkModel(
        id = id.default(),
        name = name.default(),
        category = category.default(),
        instructions = instructions?.map { it.toInstructionModel() } ?: listOf(),
        instructionsIt = instructionsIt?.map { it.toInstructionModel() } ?: listOf(),
        glass = glass.default(),
        isAlcoholic = isAlcoholic.default(false),
        imageUrl = imageLink.default(),
        videoUrl = videoLink.default(),
        type = type.default(),
        method = method.toMethod(),
        ingredients = ingredients?.ingredients?.map {
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
        visualizations = visualizations.default(),
        username = username.default(),
        userId = userId.default()
    )

    private fun String?.toMethod(): String = this?.replace(" and ", " & ").default()
    private fun String?.toClMeasure(): String? =
        this?.let { if (it.contains("-")) null else it.plus("cl") }

    private fun String?.toOzMeasure(): String? =
        this?.let { if (it.contains("-")) null else it.plus("oz") }

    private fun MainSpeakEazyBEInstructionDTO.toInstructionModel() = InstructionModel(
        type = this.type.default(),
        instruction = this.instruction.default()
    )
}