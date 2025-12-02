package com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers

import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.HomeSectionDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEIngredientDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEIngredientsListDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeDataMapperTest {

    @Before
    fun setUp() {}

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_whenIngredientsNull_ingredientsIsEmptyList() {
        // given
        val input = createHomeSectionInput()

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.sections.first().cocktails.first().ingredients.isEmpty(),
            result.sections.first().id == "1",
            result.sections.first().name == "testSectionName",
            result.sections.first().cocktails.first().id == "1",
            result.sections.first().cocktails.first().name == "testCocktailName",
            result.sections.first().cocktails.first().category == "testCategory",
            result.sections.first().cocktails.first().instructions == "testInstructionsIt",
            result.sections.first().cocktails.first().glass == "testGlass",
            result.sections.first().cocktails.first().isAlcoholic,
            result.sections.first().cocktails.first().imageUrl == "testImageLink",
            result.sections.first().cocktails.first().type == "testType",
            result.sections.first().cocktails.first().method == "testMethod & test"
        )
    }

    @Test
    fun test_mapInto_whenIngredientsNotNull_ingredientsIsNotEmpty() {
        // given
        val input = createHomeSectionInput(
            MainSpeakEazyBEIngredientsListDTO(
                ingredients = listOf(
                    MainSpeakEazyBEIngredientDTO(
                        id = "1",
                        name = "testIngredientName",
                        imageUrl = "testIngredientImageUrl",
                        quantityCl = "1",
                        quantityOz = "1",
                        quantitySpecial = "1"
                    )
                )
            ),
            method = null
        )

        // when
        val sut = createSut()
        val result = sut.mapInto(input)
        val ingredients = result.sections.first().cocktails.first().ingredients

        // then
        assertAllTrue(
            ingredients.isNotEmpty(),
            ingredients.first().id == "1",
            ingredients.first().name == "testIngredientName",
            ingredients.first().imageUrl == "testIngredientImageUrl",
            ingredients.first().measureCl == "1cl",
            ingredients.first().measureOz == "1oz",
            ingredients.first().measureSpecial == "1",
            result.sections.first().cocktails.first().method == ""
        )
    }

    @Test
    fun test_mapInto_whenIngredientsNotNull_ingredientsIsNotEmpty_checkMappingForNulls() {
        // given
        val input = createHomeSectionInput(
            MainSpeakEazyBEIngredientsListDTO(
                ingredients = listOf(
                    MainSpeakEazyBEIngredientDTO(
                        id = "1",
                        name = "testIngredientName",
                        imageUrl = "testIngredientImageUrl",
                        quantityCl = null,
                        quantityOz = null,
                        quantitySpecial = "1"
                    )
                )
            )
        )

        // when
        val sut = createSut()
        val result = sut.mapInto(input)
        val ingredients = result.sections.first().cocktails.first().ingredients

        // then
        assertAllTrue(
            ingredients.isNotEmpty(),
            ingredients.first().id == "1",
            ingredients.first().name == "testIngredientName",
            ingredients.first().imageUrl == "testIngredientImageUrl",
            ingredients.first().measureCl == null,
            ingredients.first().measureOz == null,
            ingredients.first().measureSpecial == "1"
        )
    }

    private fun createSut() = HomeDataMapper()

    private fun createHomeSectionInput(
        ingredients: MainSpeakEazyBEIngredientsListDTO? = null,
        method: String? = "testMethod and test"
    ) = HomeSectionResponseDTO(
        sections = listOf(
            HomeSectionDTO(
                id = "1",
                name = "testSectionName",
                cocktails = listOf(
                    MainSpeakEazyBEResponseDTO(
                        id = "1",
                        name = "testCocktailName",
                        category = "testCategory",
                        instructions = "testInstructions",
                        instructionsIt = "testInstructionsIt",
                        glass = "testGlass",
                        isAlcoholic = true,
                        imageLink = "testImageLink",
                        type = "testType",
                        method = method,
                        ingredients = ingredients,
                        visualizations = 1,
                        userId = "",
                        username = ""
                    )
                )
            )
        )
    )
}