package com.zioanacleto.speakeazy.ui.presentation.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zioanacleto.speakeazy.ui.presentation.main.presentation.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

@Serializable
data object MainBaseRoute

fun NavController.navigateToMain(navOptions: NavOptions) =
    navigate(route = MainRoute, navOptions = navOptions)

fun NavGraphBuilder.mainSection(
    onCocktailClick: (String) -> Unit,
    cocktailDestination: NavGraphBuilder.() -> Unit
) {
    navigation<MainBaseRoute>(startDestination = MainRoute) {
        composable<MainRoute> {
            MainScreen(
                onCocktailClick = onCocktailClick
            )
        }
        cocktailDestination()
    }
}