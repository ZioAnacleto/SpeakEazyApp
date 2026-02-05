package com.zioanacleto.speakeazy.ui.presentation.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.zioanacleto.speakeazy.ui.presentation.favorites.presentation.FavoritesScreen
import kotlinx.serialization.Serializable

@Serializable
object FavoritesRoute

@Serializable
object FavoritesBaseRoute

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) =
    navigate(route = FavoritesRoute, navOptions = navOptions)

fun NavGraphBuilder.favoritesSection(
    onCocktailClick: (String) -> Unit
) {
    navigation<FavoritesBaseRoute>(startDestination = FavoritesRoute) {
        composable<FavoritesRoute> {
            FavoritesScreen(
                onCocktailClick = onCocktailClick
            )
        }
    }
}