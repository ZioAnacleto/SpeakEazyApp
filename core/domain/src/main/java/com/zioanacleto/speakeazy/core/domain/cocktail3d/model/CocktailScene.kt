package com.zioanacleto.speakeazy.core.domain.cocktail3d.model

enum class InstructionType {
    INGREDIENTS_AND_ICE,
    SHAKE_AND_STRAIN,
    GARNISH,
    INGREDIENT,
    MUDDLE,
    SHAKE,
    STIR,
    STRAIN,
    RIM
}

data class Coordinate3D(
    val x: Float,
    val y: Float,
    val z: Float
)

/**
 *  Test implementation for now
 */
sealed class CocktailScene(
    val centerPosition: Coordinate3D,
    val cameraPosition: Coordinate3D
) {
    // this is ok for starting, without any instruction selected
    object DefaultScene : CocktailScene(
        centerPosition = Coordinate3D(0f, 0.33f, 0f),
        cameraPosition = Coordinate3D(0f, 0.5f, 2.0f)
    )
    object Scene2 : CocktailScene(
        centerPosition = Coordinate3D(0f, 0.5f, -0.3f),
        cameraPosition = Coordinate3D(0f, 0.6f, 1.4f)
    )
    object Scene3 : CocktailScene(
        centerPosition = Coordinate3D(0.2f, 0.4f, 0.1f),
        cameraPosition = Coordinate3D(0f, 0.7f, 1.8f)
    )
    // this is ok
    object GarnishScene : CocktailScene(
        centerPosition = Coordinate3D(x = 0f, y = 0.65f, z = 0f),
        cameraPosition = Coordinate3D(x = 0f, y = 0.5f, z = 0.6f)
    )
    // almost ok
    object MuddleScene : CocktailScene(
        centerPosition = Coordinate3D(x = 0f, y = 0.5f, z = 0f),
        cameraPosition = Coordinate3D(0.2f, 0.9f, 0.2f)
    )
}