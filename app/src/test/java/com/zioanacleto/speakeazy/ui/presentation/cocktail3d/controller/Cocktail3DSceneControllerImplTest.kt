package com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.TestDispatcherProvider
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.CocktailInfo
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.Cocktail3DModel
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.CocktailScene
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class Cocktail3DSceneControllerImplTest {

    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var cocktailModelDataMapper: DataMapper<CocktailInfo, Cocktail3DModel>
    private lateinit var cocktailSceneDataMapper: DataMapper<String, CocktailScene>

    private lateinit var sut: Cocktail3DSceneControllerImpl

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        cocktailModelDataMapper = mockk()
        cocktailSceneDataMapper = mockk()

        sut = Cocktail3DSceneControllerImpl(
            dispatcherProvider,
            cocktailModelDataMapper,
            cocktailSceneDataMapper
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `updateCurrentModel should update currentModel state`() = runTest {
        // Given
        val cocktailInfo = CocktailInfo("test", "test")
        val expectedModel = Cocktail3DModel.Default
        coEvery { cocktailModelDataMapper.mapInto(cocktailInfo) } returns expectedModel

        // When
        sut.updateCurrentModel(cocktailInfo)

        // Then
        val actualModel = sut.currentModel.first()
        assertEquals(expectedModel, actualModel)
    }

    @Test
    fun `updateCurrentScene should update currentScene state`() = runTest {
        // Given
        val newSceneType = "new_scene"
        val expectedScene = CocktailScene.DefaultScene
        coEvery { cocktailSceneDataMapper.mapInto(newSceneType) } returns expectedScene

        // When
        sut.updateCurrentScene(newSceneType)

        // Then
        val actualScene = sut.currentScene.first()
        assertEquals(expectedScene, actualScene)
    }
}
