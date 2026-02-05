package com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper

import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.CocktailScene
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CocktailSceneDataMapperTest(
    private val inputString: String,
    private val expectedResult: CocktailScene
) {
    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `mapInto - should return correct data`() {
        // when
        val sut = createSut()
        val response = sut.mapInto(inputString)

        // then
        assertEquals(expectedResult, response)
    }

    private fun createSut() = CocktailSceneDataMapper()

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} should return {1}")
        fun provideData() = listOf(
            arrayOf("ingredientsAndIce", CocktailScene.DefaultScene),
            arrayOf("ingredientsWithIce", CocktailScene.DefaultScene),
            arrayOf("shakeAndStrain", CocktailScene.Scene2),
            arrayOf("garnish", CocktailScene.GarnishScene),
            arrayOf("muddle", CocktailScene.MuddleScene),
            arrayOf("shake", CocktailScene.DefaultScene),
            arrayOf("stir", CocktailScene.DefaultScene),
            arrayOf("strain", CocktailScene.DefaultScene),
            arrayOf("rim", CocktailScene.DefaultScene),
            arrayOf("ingredient", CocktailScene.DefaultScene)
        )
    }
}