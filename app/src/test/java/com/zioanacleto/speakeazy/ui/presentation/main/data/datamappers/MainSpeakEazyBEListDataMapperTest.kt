package com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers

import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEIngredientsListDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEInstructionDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class MainSpeakEazyBEListDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto() {
        // given
        val input = createInput("testMethod and other")

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assertAllTrue(
            response.drinks.size == 1,
            response.drinks.first().id == "1",
            response.drinks.first().name == "testName",
            response.drinks.first().method == "testMethod & other",
            response.drinks.first().imageUrl == "testImageLink",
            response.drinks.first().isAlcoholic
        )
    }

    @Test
    fun test_mapInto_categoryIsNull() {
        // given
        val input = createInput(null)

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assertAllTrue(
            response.drinks.size == 1,
            response.drinks.first().id == "1",
            response.drinks.first().name == "testName",
            response.drinks.first().method == "",
            response.drinks.first().imageUrl == "testImageLink",
            response.drinks.first().isAlcoholic
        )
    }

    private fun createSut() = MainSpeakEazyBEListDataMapper()

    private fun createInput(method: String? = null) =
        MainSpeakEazyBEListResponseDTO(
            cocktails = listOf(
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
                    method = method,
                    ingredients = MainSpeakEazyBEIngredientsListDTO(
                        ingredients = listOf()
                    ),
                    visualizations = 1L,
                    username = "testUsername",
                    userId = "testUserId"
                )
            )
        )
}