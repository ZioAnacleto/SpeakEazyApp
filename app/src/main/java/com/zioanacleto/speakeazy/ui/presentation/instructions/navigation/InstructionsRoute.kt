package com.zioanacleto.speakeazy.ui.presentation.instructions.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.zioanacleto.speakeazy.ui.presentation.instructions.InstructionsScreen
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.InstructionModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val KEY_INSTRUCTIONS = "instructions"

@Serializable
data object InstructionsRoute

fun NavController.navigateToInstructions(
    instructions: List<InstructionModel>,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    // Convert the list of InstructionModel to a JSON string to be passed as parameter
    val json = Json.encodeToString(instructions)
    currentBackStackEntry?.savedStateHandle?.set(
        KEY_INSTRUCTIONS,
        json
    )
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
        val instructions =
            navController.previousBackStackEntry?.savedStateHandle?.get<String>(KEY_INSTRUCTIONS)
        instructions?.let {
            val parsedInstructions = Json.decodeFromString<List<InstructionModel>>(it)
            InstructionsScreen(
                instructions = parsedInstructions,
                onBackButton = onBackButtonClick
            )
        }
    }
}