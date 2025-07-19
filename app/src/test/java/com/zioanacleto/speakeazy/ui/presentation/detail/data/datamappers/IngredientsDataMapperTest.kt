package com.zioanacleto.speakeazy.ui.presentation.detail.data.datamappers

import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientDTO
import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientsListDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IngredientsDataMapperTest {

    @Before
    fun setup() {}

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_sizeIs1() {
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

        val output = sut.mapInto(input)

        assertEquals(1, output.ingredients.size)
    }

    @Test
    fun test_mapInto_sizeIs0() {
        val input = IngredientsListDTO(
            ingredients = null
        )

        val sut = createSut()

        val output = sut.mapInto(input)

        assertEquals(0, output.ingredients.size)
    }

    private fun createSut() = IngredientsDataMapper()
}