package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller

import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.CocktailInfo
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.Cocktail3DModel
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.CocktailScene
import kotlinx.coroutines.flow.StateFlow

interface Cocktail3DSceneController {
    val currentModel: StateFlow<Cocktail3DModel>
    val currentScene: StateFlow<CocktailScene>

    fun updateCurrentModel(cocktailInfo: CocktailInfo)
    fun updateCurrentScene(newSceneType: String)
}