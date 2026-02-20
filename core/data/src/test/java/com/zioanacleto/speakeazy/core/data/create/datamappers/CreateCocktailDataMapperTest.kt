package com.zioanacleto.speakeazy.core.data.create.datamappers

import com.zioanacleto.speakeazy.core.data.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test
import java.util.Date

class CreateCocktailDataMapperTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `mapInto - should return converted data`() {
        // given
        val input = CreateCocktailModel(
            currentStep = 0,
            createdTime = Date(),
            lastUpdateTime = Date(),
            cocktailName = "testCocktailName",
            isAlcoholic = true,
            type = "testType",
            method = "testMethod",
            glass = "testGlass",
            ingredients = mapOf("testIngredient" to "1 cl"),
            instructions = "testInstructions",
            instructionsIt = "testInstructionsIt",
            imageUrl = "testImageUrl",
            videoUrl = "testVideoUrl",
            userId = "testUserId",
            username = "testUsername"
        )

        // when
        val sut = createSut()
        val result = sut.mapInto(input)

        // then
        with(result) {
            assertAllTrue(
                name == "testCocktailName",
                isAlcoholic,
                id == "1000",
                category == "SpeakEazy Original",
                type == "testType",
                method == "testMethod",
                glass == "testGlass",
                instructions.first().instruction == "testInstructions",
                instructionsIt.first().instruction == "testInstructionsIt",
                imageLink == "testImageUrl",
                videoLink == "testVideoUrl",
                userId == "testUserId",
                username == "testUsername"
            )
        }
    }

    private fun createSut() = CreateCocktailDataMapper()
}