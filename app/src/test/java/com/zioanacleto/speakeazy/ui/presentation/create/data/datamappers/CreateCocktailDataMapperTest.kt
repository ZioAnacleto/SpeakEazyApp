package com.zioanacleto.speakeazy.ui.presentation.create.data.datamappers

import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
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
    fun test_mapInto() {
        // given
        val input = createInput()

        // when
        val sut = createSut()
        val response = sut.mapInto(input)

        // then
        assertAllTrue(
            response.id == "1000",
            response.name == "testName",
            response.instructions.first().instruction == "testInstructions",
            response.instructionsIt.first().instruction == "testInstructionsIt",
            response.glass == "testGlass",
            response.isAlcoholic,
            response.imageLink == "testImageUrl",
            response.type == "testType",
            response.method == "testMethod",
            response.ingredients.ingredients?.first()?.id == "testIngredient",
            response.ingredients.ingredients?.first()?.quantityCl == "testMeasure",
            response.ingredients.ingredients?.first()?.quantityOz == "-",
            response.ingredients.ingredients?.first()?.quantitySpecial == null,
            response.username == "testUsername",
            response.userId == "testUserId"
        )
    }

    private fun createInput() = CreateCocktailModel(
        id = 1,
        currentStep = CreateWizardStepData.First,
        createdTime = Date(),
        lastUpdateTime = Date(),
        cocktailName = "testName",
        isAlcoholic = true,
        instructions = "testInstructions",
        instructionsIt = "testInstructionsIt",
        glass = "testGlass",
        ingredients = mapOf("testIngredient" to "testMeasure cl"),
        imageUrl = "testImageUrl",
        type = "testType",
        method = "testMethod",
        userId = "testUserId",
        username = "testUsername"
    )

    private fun createSut() = CreateCocktailDataMapper()
}