package com.zioanacleto.speakeazy.ui.presentation.instructions.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.domain.main.model.InstructionModel
import com.zioanacleto.speakeazy.ui.presentation.instructions.InstructionsScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val KEY_GLASS_TYPE = "glassType"
private const val KEY_INSTRUCTIONS = "instructions"

@Serializable
data object InstructionsRoute

fun NavController.navigateToInstructions(
    glassType: String,
    instructions: List<InstructionModel>,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    saveData(KEY_GLASS_TYPE, glassType)
    // Convert the list of InstructionModel to a JSON string to be passed as parameter
    val json = Json.encodeToString(instructions)
    saveData(KEY_INSTRUCTIONS, json)
    navigate(route = InstructionsRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.instructionsSection(
    navController: NavController,
    onBackButtonClick: () -> Unit
) {
    composable<InstructionsRoute>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        with(navController) {
            val glassType = retrieveData(KEY_GLASS_TYPE)
            val instructions = retrieveData(KEY_INSTRUCTIONS)
            instructions?.let {
                val parsedInstructions = Json.decodeFromString<List<InstructionModel>>(it)
                InstructionsScreen(
                    glassType = glassType.default(),
                    instructions = parsedInstructions,
                    onBackButton = onBackButtonClick
                )
            }
        }
    }
}

private fun NavController.saveData(key: String, value: String) {
    currentBackStackEntry?.savedStateHandle?.set(
        key,
        value
    )
}

private fun NavController.retrieveData(key: String) =
    previousBackStackEntry?.savedStateHandle?.get<String>(key)