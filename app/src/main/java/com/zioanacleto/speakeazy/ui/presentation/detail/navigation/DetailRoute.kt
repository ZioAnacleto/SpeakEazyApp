package com.zioanacleto.speakeazy.ui.presentation.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zioanacleto.speakeazy.ui.presentation.detail.presentation.DetailScreen
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.InstructionModel
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val id: String)

fun NavController.navigateToDetail(
    cocktailId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = DetailRoute(cocktailId)) {
        navOptions()
    }
}

fun NavGraphBuilder.detailSection(
    showTopBar: Boolean,
    onBackButtonClick: () -> Unit,
    onInstructionsClick: (List<InstructionModel>) -> Unit
) {
    composable<DetailRoute> { entry ->
        val cocktailId = entry.toRoute<DetailRoute>().id
        DetailScreen(
            cocktailId = cocktailId,
            onInstructionsClick = onInstructionsClick,
            onBackButtonClick = onBackButtonClick
        )
    }
}