package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model

enum class GlassType {
    DEFAULT,
    HIGHBALL,
    COCKTAIL,
    COPPA_MARTINI,
    COLLINS,
}

/**
 *  TODO: TO BE IMPLEMENTED
 */
sealed class Cocktail3DModel(
    val sceneModelName: String,
    val cocktailColor: String,
    val withIce: Boolean
) {
    data class CoppaMartini(
        val ice: Boolean,
        val color: String
    ): Cocktail3DModel(
        sceneModelName = "martini_glass_scene.glb",
        cocktailColor = color,
        withIce = ice
    )
    object Default: Cocktail3DModel(
        sceneModelName = "cocktail_scene.glb",
        cocktailColor = "white",
        withIce = false
    )
}

