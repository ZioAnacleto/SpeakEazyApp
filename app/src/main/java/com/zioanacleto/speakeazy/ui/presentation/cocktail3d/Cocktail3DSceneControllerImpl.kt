package com.zioanacleto.speakeazy.ui.presentation.cocktail3d

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.model.CocktailScene
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Cocktail3DSceneControllerImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val dataMapper: DataMapper<String, CocktailScene>
) : Cocktail3DSceneController {
    private val _currentScene = MutableStateFlow<CocktailScene>(CocktailScene.DefaultScene)
    override val currentScene: StateFlow<CocktailScene> = _currentScene.asStateFlow()

    override fun updateCurrentScene(newSceneType: String) {
        CoroutineScope(dispatcherProvider.io()).launch {
            _currentScene.emit(dataMapper.mapInto(newSceneType))
        }
    }
}