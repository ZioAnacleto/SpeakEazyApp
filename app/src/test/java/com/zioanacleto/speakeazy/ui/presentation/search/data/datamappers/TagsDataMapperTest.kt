package com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers

import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.TagDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.TagsResponseDTO
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class TagsDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto_whenResponseIsEmpty_tagsIsEmpty() {
        // given
        val input = TagsResponseDTO(
            tags = listOf()
        )

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assert(response.tags.isEmpty())
    }

    @Test
    fun test_mapInto_whenResponseIsNotEmpty_tagsIsNotEmpty() {
        // given
        val input = TagsResponseDTO(
            tags = listOf(
                TagDTO(
                    id = "1",
                    name = "testName"
                )
            )
        )

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assertAllTrue(
            response.tags.isNotEmpty(),
            response.tags.first().id == "1",
            response.tags.first().name == "testName"
        )
    }

    private fun createSut() = TagsDataMapper()
}