package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.model

import io.github.sceneview.math.Position

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

/**
 *  Test implementation for now
 */
sealed class CocktailScene(
    val centerPosition: Position,
    val cameraPosition: Position
) {
    // this is ok for starting, without any instruction selected
    object DefaultScene : CocktailScene(
        centerPosition = Position(0f, 0.33f, 0f),
        cameraPosition = Position(0f, 0.5f, 2.0f)
    )
    object Scene2 : CocktailScene(
        centerPosition = Position(0f, 0.5f, -0.3f),
        cameraPosition = Position(0f, 0.6f, 1.4f)
    )
    object Scene3 : CocktailScene(
        centerPosition = Position(0.2f, 0.4f, 0.1f),
        cameraPosition = Position(0f, 0.7f, 1.8f)
    )
    // this is ok
    object GarnishScene : CocktailScene(
        centerPosition = Position(x = 0f, y = 0.65f, z = 0f),
        cameraPosition = Position(x = 0f, y = 0.5f, z = 0.6f)
    )
    // almost ok
    object MuddleScene : CocktailScene(
        centerPosition = Position(x = 0f, y = 0.5f, z = 0f),
        cameraPosition = Position(0.2f, 0.9f, 0.2f)
    )
}