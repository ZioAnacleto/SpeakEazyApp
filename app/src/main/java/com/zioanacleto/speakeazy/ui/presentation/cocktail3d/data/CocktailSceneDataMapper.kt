package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.data

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model.CocktailScene
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model.InstructionType

/**
 *  Test implementation, to be finished
 */
class CocktailSceneDataMapper: DataMapper<String, CocktailScene> {
    override fun mapInto(instruction: String): CocktailScene {
        return when(instruction.toInstructionType()) {
            InstructionType.INGREDIENTS_AND_ICE -> CocktailScene.DefaultScene
            InstructionType.SHAKE_AND_STRAIN -> CocktailScene.Scene2
            InstructionType.GARNISH -> CocktailScene.GarnishScene
            InstructionType.MUDDLE -> CocktailScene.MuddleScene
            else -> CocktailScene.DefaultScene
        }
    }

    private fun String.toInstructionType() = when(this) {
        "ingredientsAndIce",
        "ingredientsWithIce" -> InstructionType.INGREDIENTS_AND_ICE
        "shakeAndStrain" -> InstructionType.SHAKE_AND_STRAIN
        "garnish" -> InstructionType.GARNISH
        "muddle" -> InstructionType.MUDDLE
        "shake" -> InstructionType.SHAKE
        "stir" -> InstructionType.STIR
        "strain" -> InstructionType.STRAIN
        "rim" -> InstructionType.RIM
        "ingredient" -> InstructionType.INGREDIENT
        else -> InstructionType.INGREDIENT
    }
}