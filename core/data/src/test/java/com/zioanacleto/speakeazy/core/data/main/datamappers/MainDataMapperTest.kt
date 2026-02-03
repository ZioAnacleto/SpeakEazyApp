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
    fun `mapInto - when input is not null return mapped fields`() {
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
            result.drinks.first().instructions.first().instruction == "testInstructions1",
            result.drinks.first().glass == "testGlass",
            result.drinks.first().imageUrl == "testImageSource",
            result.drinks.first().ingredients.size == 10,
            result.drinks.first().ingredients[0].name == "testIngredient1",
            result.drinks.first().ingredients[0].measureCl == "testMeasure1",
            result.drinks.first().ingredients[1].name == "testIngredient2",
            result.drinks.first().ingredients[1].measureCl == "testMeasure2",
            result.drinks.first().ingredients[2].name == "testIngredient3",
            result.drinks.first().ingredients[2].measureCl == "testMeasure3",
            result.drinks.first().ingredients[3].name == "testIngredient4",
            result.drinks.first().ingredients[3].measureCl == "testMeasure4",
            result.drinks.first().ingredients[4].name == "testIngredient5",
            result.drinks.first().ingredients[4].measureCl == "testMeasure5",
            result.drinks.first().ingredients[5].name == "testIngredient6",
            result.drinks.first().ingredients[5].measureCl == "testMeasure6",
            result.drinks.first().ingredients[6].name == "testIngredient7",
            result.drinks.first().ingredients[6].measureCl == "testMeasure7",
            result.drinks.first().ingredients[7].name == "testIngredient8",
            result.drinks.first().ingredients[7].measureCl == "testMeasure8",
            result.drinks.first().ingredients[8].name == "testIngredient9",
            result.drinks.first().ingredients[8].measureCl == "testMeasure9",
            result.drinks.first().ingredients[9].name == "testIngredient10",
            result.drinks.first().ingredients[9].measureCl == "testMeasure10"
        )
    }

    @Test
    fun `mapInto - when input fields are null return null fields`() {
        // given
        val input = createNullInput()

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.drinks.size == 1,
            result.drinks.first().name == "",
            result.drinks.first().category == "",
            result.drinks.first().instructions.first().instruction == "",
            result.drinks.first().glass == "",
            result.drinks.first().imageUrl == "",
            result.drinks.first().ingredients.isEmpty()
        )
    }

    @Test
    fun `mapInto - when input is null return empty list`() {
        // given
        val input = MainResponseDTO(null)

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertTrue(result.drinks.isEmpty())
    }

    private fun createSut() = MainDataMapper()

    private fun createNullInput() = MainResponseDTO(
        drinks = listOf(
            MainResponseDTO.DrinkDTO(
                idDrink = null,
                strDrink = null,
                strCategory = null,
                strInstructionsIT = null,
                strGlass = null,
                strImageSource = null,
                strIngredient1 = null,
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
                strMeasure1 = null,
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

    private fun createInput(
        id: String? = "1",
        name: String? = "test",
        category: String? = "testCategory",
        instructions: String? = "testInstructions1",
        glass: String? = "testGlass",
        imageUrl: String? = "testImageSource"
    ) = MainResponseDTO(
        drinks = listOf(
            MainResponseDTO.DrinkDTO(
                idDrink = id,
                strDrink = name,
                strCategory = category,
                strInstructionsIT = instructions,
                strGlass = glass,
                strImageSource = imageUrl,
                strIngredient1 = "testIngredient1",
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
                strIngredient2 = "testIngredient2",
                strIngredient3 = "testIngredient3",
                strIngredient4 = "testIngredient4",
                strIngredient5 = "testIngredient5",
                strIngredient6 = "testIngredient6",
                strIngredient7 = "testIngredient7",
                strIngredient8 = "testIngredient8",
                strIngredient9 = "testIngredient9",
                strIngredient10 = "testIngredient10",
                strIngredient11 = "testIngredient11",
                strIngredient12 = "testIngredient12",
                strIngredient13 = "testIngredient13",
                strIngredient14 = "testIngredient14",
                strIngredient15 = "testIngredient15",
                strMeasure1 = "testMeasure1",
                strMeasure2 = "testMeasure2",
                strMeasure3 = "testMeasure3",
                strMeasure4 = "testMeasure4",
                strMeasure5 = "testMeasure5",
                strMeasure6 = "testMeasure6",
                strMeasure7 = "testMeasure7",
                strMeasure8 = "testMeasure8",
                strMeasure9 = "testMeasure9",
                strMeasure10 = "testMeasure10",
                strMeasure11 = "testMeasure11",
                strMeasure12 = "testMeasure12",
                strMeasure13 = "testMeasure13",
                strMeasure14 = "testMeasure14",
                strMeasure15 = "testMeasure15",
                strImageAttribution = null,
                strCreativeCommonsConfirmed = null,
                dateModified = null,
            )
        )
    )
}