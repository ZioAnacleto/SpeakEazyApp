package com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers

import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class SearchRequestDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_mapInto() {
        // given
        val input = "test"

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assert(response.query == input)
    }

    private fun createSut() = SearchRequestDataMapper()
}