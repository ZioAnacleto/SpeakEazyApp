package com.zioanacleto.speakeazy.ui.presentation.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zioanacleto.speakeazy.ui.presentation.search.presentation.SearchScreen
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

@Serializable
object SearchBaseRoute

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = SearchRoute, navOptions = navOptions)

fun NavGraphBuilder.searchSection() {
    navigation<SearchBaseRoute>(startDestination = SearchRoute) {
        composable<SearchRoute> {
            SearchScreen()
        }
    }
}