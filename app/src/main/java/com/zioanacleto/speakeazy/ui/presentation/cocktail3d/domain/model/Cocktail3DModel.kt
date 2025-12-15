package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model

enum class GlassType {
    HIGHBALL,
    COCKTAIL,
    COPPA_MARTINI,
    COLLINS,
}

/**
 *  TODO: TO BE IMPLEMENTED
 */
sealed class Cocktail3DModel(
    val cocktailName: String,
    val cocktailGlass: GlassType,
    val cocktailColor: String,
    val withIce: Boolean
) {

}

