package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.models

import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.CocktailScene
import io.github.sceneview.math.Position

data class CocktailUiScene(
    val centerPosition: Position,
    val cameraPosition: Position
)

fun CocktailScene.toUiScene() = CocktailUiScene(
    centerPosition = Position(
        centerPosition.x,
        centerPosition.y,
        centerPosition.z,
    ),
    cameraPosition = Position(
        cameraPosition.x,
        cameraPosition.y,
        cameraPosition.z,
    )
)
