package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain

import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.data.CocktailInfo
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model.Cocktail3DModel
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.model.CocktailScene
import kotlinx.coroutines.flow.StateFlow

interface Cocktail3DSceneController {
    val currentModel: StateFlow<Cocktail3DModel>
    val currentScene: StateFlow<CocktailScene>

    fun updateCurrentModel(cocktailInfo: CocktailInfo)
    fun updateCurrentScene(newSceneType: String)
}