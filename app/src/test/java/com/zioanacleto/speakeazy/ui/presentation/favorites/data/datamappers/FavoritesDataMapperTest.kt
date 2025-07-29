package com.zioanacleto.speakeazy.ui.presentation.favorites.data.datamappers

import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.assertEquals
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
                    instructions = "testInstructions",
                    instructionsIt = "testInstructionsIt",
                    glass = "testGlass",
                    isAlcoholic = true,
                    imageLink = "testImageLink",
                    type = "testType",
                    method = "testMethod and test",
                    ingredients = null,
                    visualizations = 1
                )
            )
        )
        val sut = createSut()

        // when
        val result = sut.mapInto(input)

        // then
        assertEquals(1, result.favorites.size)
        assertEquals("1", result.favorites.first().id)
        assertEquals("testName", result.favorites.first().name)
        assertEquals("testCategory", result.favorites.first().category)
        assertEquals(true, result.favorites.first().isAlcoholic)
        assertEquals("testImageLink", result.favorites.first().imageUrl)
        assertEquals("testType", result.favorites.first().type)
        assertEquals("testMethod & test", result.favorites.first().method)

    }

    private fun createSut() = FavoritesDataMapper()
}