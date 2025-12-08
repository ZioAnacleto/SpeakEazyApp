package com.zioanacleto.speakeazy.ui.presentation.detail.data.datamappers

import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientDTO
import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientsListDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientsDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_sizeIs1() {
        // given
        val input = IngredientsListDTO(
            ingredients = listOf(
                IngredientDTO(
                    id = "1",
                    name = "Ingredient 1",
                    imageUrl = "https://example.com/image1.jpg"
                )
            )
        )
        val sut = createSut()

        // when
        val output = sut.mapInto(input)

        // then
        assertEquals(1, output.ingredients.size)
    }

    @Test
    fun test_mapInto_sizeIs0() {
        // given
        val input = IngredientsListDTO(
            ingredients = null
        )
        val sut = createSut()

        // when
        val output = sut.mapInto(input)

        // then
        assertEquals(0, output.ingredients.size)
    }

    private fun createSut() = IngredientsDataMapper()
}