package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain

import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model.CocktailScene
import kotlinx.coroutines.flow.StateFlow

interface Cocktail3DSceneController {
    val currentScene: StateFlow<CocktailScene>

    fun updateCurrentScene(newSceneType: String)
}