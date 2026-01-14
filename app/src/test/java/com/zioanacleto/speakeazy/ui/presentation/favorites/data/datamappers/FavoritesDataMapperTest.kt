package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datamappers

import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEInstructionDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class FavoritesDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto() {
        // given
        val input = MainSpeakEazyBEListResponseDTO(
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
                    method = "testMethod and test",
                    ingredients = null,
                    visualizations = 1,
                    userId = "",
                    username = ""
                )
            )
        )
        val sut = createSut()

        // when
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.favorites.size == 1,
            result.favorites.first().id == "1",
            result.favorites.first().name == "testName",
            result.favorites.first().category == "testCategory",
            result.favorites.first().isAlcoholic,
            result.favorites.first().imageUrl == "testImageLink",
            result.favorites.first().type == "testType",
            result.favorites.first().method == "testMethod & test"
        )
    }

    private fun createSut() = FavoritesDataMapper()
}