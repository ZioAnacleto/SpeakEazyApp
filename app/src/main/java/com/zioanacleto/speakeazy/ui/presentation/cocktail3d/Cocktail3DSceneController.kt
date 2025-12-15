package com.zioanacleto.speakeazy.ui.presentation.cocktail3d

import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.model.CocktailScene
import kotlinx.coroutines.flow.StateFlow

interface Cocktail3DSceneController {
    val currentScene: StateFlow<CocktailScene>

    fun updateCurrentScene(newSceneType: String)
}