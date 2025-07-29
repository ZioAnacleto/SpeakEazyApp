package com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers

import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainResponseDTO
import io.mockk.clearAllMocks
import org.junit.Assert.*

import org.junit.After
import org.junit.Test

class MainListDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_whenInputIsNotNull_returnMappedFields() {
        // given
        val input = MainListResponseDTO(
            drinks = listOf(
                MainListResponseDTO.SimpleDrinkDTO(
                    idDrink = "1",
                    strDrink = "test",
                    strDrinkThumb = "testThumb"
                )
            )
        )

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertAllTrue(
            result.drinks.size == 1,
            result.drinks.first().id == "1",
            result.drinks.first().name == "test",
            result.drinks.first().imageUrl == "testThumb"
        )
    }

    @Test
    fun test_mapInto_whenInputIsNull_returnEmptyList() {
        // given
        val input = MainListResponseDTO(
            drinks = null
        )

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        assertTrue(result.drinks.isEmpty())
    }

    private fun createSut() = MainListDataMapper()
}