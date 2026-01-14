package com.zioanacleto.speakeazy.core.data.main.datamappers

import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.main.dto.MainResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test

class MainDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_whenInputIsNotNull_returnMappedFields() {
        // given
        val input = createInput()

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.drinks.size == 1,
            result.drinks.first().name == "test",
            result.drinks.first().category == "testCategory",
            result.drinks.first().instructions == "testInstructions1",
            result.drinks.first().glass == "testGlass",
            result.drinks.first().imageUrl == "testImageSource",
            result.drinks.first().ingredients.size == 1,
            result.drinks.first().ingredients[0].name == "testIngredient",
            result.drinks.first().ingredients[0].measureCl == "testMeasure1"
        )
    }

    @Test
    fun test_mapInto_whenInputFieldsAreNull_returnNullFields() {
        // given
        val input = createInput(
            id = null,
            name = null,
            category = null,
            instructions = null,
            glass = null,
            imageUrl = null,
            ingredient1 = null,
            measure1 = null
        )

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.drinks.size == 1,
            result.drinks.first().name == "",
            result.drinks.first().category == "",
            result.drinks.first().instructions == "",
            result.drinks.first().glass == "",
            result.drinks.first().imageUrl == "",
            result.drinks.first().ingredients.isEmpty()
        )
    }

    @Test
    fun test_mapInto_whenInputIsNull_returnEmptyList() {
        // given
        val input = MainResponseDTO(null)

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertTrue(result.drinks.isEmpty())
    }

    private fun createSut() = MainDataMapper()

    private fun createInput(
        id: String? = "1",
        name: String? = "test",
        category: String? = "testCategory",
        instructions: String? = "testInstructions1",
        glass: String? = "testGlass",
        imageUrl: String? = "testImageSource",
        ingredient1: String? = "testIngredient",
        measure1: String? = "testMeasure1"
    ) = MainResponseDTO(
        drinks = listOf(
            MainResponseDTO.DrinkDTO(
                idDrink = id,
                strDrink = name,
                strCategory = category,
                strInstructionsIT = instructions,
                strGlass = glass,
                strImageSource = imageUrl,
                strIngredient1 = ingredient1,
                strDrinkAlternate = null,
                strTags = null,
                strVideo = null,
                strIBA = null,
                strAlcoholic = null,
                strInstructions = null,
                strInstructionsES = null,
                strInstructionsDE = null,
                strInstructionsFR = null,
                strInstructionsZHHANS = null,
                strInstructionsZHHANT = null,
                strDrinkThumb = null,
                strIngredient2 = null,
                strIngredient3 = null,
                strIngredient4 = null,
                strIngredient5 = null,
                strIngredient6 = null,
                strIngredient7 = null,
                strIngredient8 = null,
                strIngredient9 = null,
                strIngredient10 = null,
                strIngredient11 = null,
                strIngredient12 = null,
                strIngredient13 = null,
                strIngredient14 = null,
                strIngredient15 = null,
                strMeasure1 = measure1,
                strMeasure2 = null,
                strMeasure3 = null,
                strMeasure4 = null,
                strMeasure5 = null,
                strMeasure6 = null,
                strMeasure7 = null,
                strMeasure8 = null,
                strMeasure9 = null,
                strMeasure10 = null,
                strMeasure11 = null,
                strMeasure12 = null,
                strMeasure13 = null,
                strMeasure14 = null,
                strMeasure15 = null,
                strImageAttribution = null,
                strCreativeCommonsConfirmed = null,
                dateModified = null,
            )
        )
    )
}