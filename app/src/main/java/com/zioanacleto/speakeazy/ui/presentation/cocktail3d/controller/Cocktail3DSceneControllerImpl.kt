package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.CocktailInfo
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.Cocktail3DModel
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.CocktailScene
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Cocktail3DSceneControllerImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val cocktailModelDataMapper: DataMapper<CocktailInfo, Cocktail3DModel>,
    private val cocktailSceneDataMapper: DataMapper<String, CocktailScene>
) : Cocktail3DSceneController {
    private val _currentModel = MutableStateFlow<Cocktail3DModel>(Cocktail3DModel.Default)
    override val currentModel: StateFlow<Cocktail3DModel> = _currentModel.asStateFlow()

    private val _currentScene = MutableStateFlow<CocktailScene>(CocktailScene.DefaultScene)
    override val currentScene: StateFlow<CocktailScene> = _currentScene.asStateFlow()

    override fun updateCurrentModel(cocktailInfo: CocktailInfo) {
        CoroutineScope(dispatcherProvider.io()).launch {
            _currentModel.emit(cocktailModelDataMapper.mapInto(cocktailInfo))
        }
    }

    override fun updateCurrentScene(newSceneType: String) {
        CoroutineScope(dispatcherProvider.io()).launch {
            _currentScene.emit(cocktailSceneDataMapper.mapInto(newSceneType))
        }
    }
}