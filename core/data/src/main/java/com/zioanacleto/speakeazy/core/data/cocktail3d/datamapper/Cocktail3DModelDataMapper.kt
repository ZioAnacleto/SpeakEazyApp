package com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.Cocktail3DModel
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.GlassType

// pair with glassType and color
typealias CocktailInfo = Pair<String, String>

class Cocktail3DModelDataMapper : DataMapper<CocktailInfo, Cocktail3DModel> {
    override fun mapInto(input: CocktailInfo): Cocktail3DModel {
        val (glass, color) = input
        return when (glass.toGlassType()) {
            GlassType.COPPA_MARTINI -> Cocktail3DModel.CoppaMartini(ice = true, color = color)
            else -> Cocktail3DModel.Default
        }
    }

    private fun String.toGlassType() = when (this) {
        "Cocktail glass" -> GlassType.COCKTAIL
        "Highball glass" -> GlassType.HIGHBALL
        "Coppa Martini" -> GlassType.COPPA_MARTINI
        "Collins glass" -> GlassType.COLLINS
        else -> GlassType.DEFAULT
    }
}