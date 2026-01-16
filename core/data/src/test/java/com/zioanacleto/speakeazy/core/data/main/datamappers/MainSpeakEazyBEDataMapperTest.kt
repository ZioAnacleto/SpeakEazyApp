package com.zioanacleto.speakeazy.core.data.main.datamappers

import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEIngredientDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEIngredientsListDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEInstructionDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class MainSpeakEazyBEDataMapperTest {
    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto() {
        // given
        val input = createInput()

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assertAllTrue(
            response.drinks.size == 1,
            response.drinks.first().id == "1",
            response.drinks.first().name == "testName",
            response.drinks.first().category == "testCategory",
            response.drinks.first().instructions.first().instruction == "testInstructions",
            response.drinks.first().instructionsIt.first().instruction == "testInstructionsIt",
            response.drinks.first().glass == "testGlass",
            response.drinks.first().isAlcoholic,
            response.drinks.first().imageUrl == "testImageLink",
            response.drinks.first().type == "testType",
            response.drinks.first().method == "testMethod",
            response.drinks.first().ingredients.isEmpty(),
            response.drinks.first().visualizations == 1L,
            response.drinks.first().username == "testUsername",
            response.drinks.first().userId == "testUserId"
        )
    }

    @Test
    fun test_mapInto_verifyIngredientsMapping() {
        // given
        val input = createInput(
            listOf(
                MainSpeakEazyBEIngredientDTO(
                    id = "1",
                    name = "testNameFirst",
                    imageUrl = "testImageUrlFirst",
                    quantityCl = "1",
                    quantityOz = "2",
                    quantitySpecial = "testQuantitySpecialFirst"
                ),
                MainSpeakEazyBEIngredientDTO(
                    id = "2",
                    name = "testNameSecond",
                    imageUrl = "testImageUrlSecond",
                    quantityCl = "-",
                    quantityOz = "-",
                    quantitySpecial = "testQuantitySpecialSecond"
                )
            )
        )

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assertAllTrue(
            response.drinks.first().ingredients.first().id == "1",
            response.drinks.first().ingredients.first().name == "testNameFirst",
            response.drinks.first().ingredients.first().imageUrl == "testImageUrlFirst",
            response.drinks.first().ingredients.first().measureCl == "1cl",
            response.drinks.first().ingredients.first().measureOz == "2oz",
            response.drinks.first().ingredients.first().measureSpecial == "testQuantitySpecialFirst",
            response.drinks.first().ingredients[1].id == "2",
            response.drinks.first().ingredients[1].name == "testNameSecond",
            response.drinks.first().ingredients[1].imageUrl == "testImageUrlSecond",
            response.drinks.first().ingredients[1].measureCl == null,
            response.drinks.first().ingredients[1].measureOz == null,
            response.drinks.first().ingredients[1].measureSpecial == "testQuantitySpecialSecond"
        )
    }

    @Test
    fun test_mapInto_verifyIngredientsListNull() {
        // given
        val input = createInput(null)

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assert(response.drinks.first().ingredients.isEmpty())
    }

    private fun createSut() = MainSpeakEazyBEDataMapper()

    private fun createInput(ingredients: List<MainSpeakEazyBEIngredientDTO>? = listOf()) =
        MainSpeakEazyBEResponseDTO(
            id = "1",
            name = "testName",
            category = "testCategory",
            instructions = listOf(
                MainSpeakEazyBEInstructionDTO(
                    type = "testType",
                    "testInstructions"
                )
            ),
            instructionsIt = listOf(
                MainSpeakEazyBEInstructionDTO(
                    type = "testType",
                    "testInstructionsIt"
                )
            ),
            glass = "testGlass",
            isAlcoholic = true,
            imageLink = "testImageLink",
            type = "testType",
            method = "testMethod",
            ingredients = MainSpeakEazyBEIngredientsListDTO(
                ingredients = ingredients
            ),
            visualizations = 1L,
            username = "testUsername",
            userId = "testUserId"
        )
}