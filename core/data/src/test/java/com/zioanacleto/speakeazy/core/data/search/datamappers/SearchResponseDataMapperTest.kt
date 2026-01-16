package com.zioanacleto.speakeazy.core.data.search.datamappers

import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.core.data.search.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.core.domain.search.model.SearchItem
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class SearchResponseDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_whenInputCocktailIsEmpty_resultsIsEmpty() {
        // given
        val input = SearchResponseDTO(
            cocktails = listOf()
        )

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assert(response.results.isEmpty())
    }

    @Test
    fun test_mapInto_whenInputCocktailIsNotEmpty_resultsIsNotEmpty() {
        // given
        val input = SearchResponseDTO(
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

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.results.size == 1,
            result.results.first() is SearchItem.Cocktail
        )
    }

    private fun createSut() = SearchResponseDataMapper()
}