package com.zioanacleto.speakeazy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.zioanacleto.speakeazy.SpeakEazyAppState
import com.zioanacleto.speakeazy.ui.presentation.detail.navigation.detailSection
import com.zioanacleto.speakeazy.ui.presentation.detail.navigation.navigateToDetail
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.favoritesSection
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.MainBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.mainSection
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.searchSection
import com.zioanacleto.speakeazy.ui.presentation.user.navigation.userSection
import kotlin.reflect.KClass

@Composable
fun SpeakEazyNavHost(
    appState: SpeakEazyAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = MainBaseRoute,
        modifier = modifier
    ) {
        mainSection(
            onCocktailClick = navController::navigateToDetail
        ) {
            detailSection(
                showTopBar = false,
                onBackButtonClick = navController::popBackStack
            )
        }
        favoritesSection(
            onCocktailClick = navController::navigateToDetail
        ) {
            detailSection(
                showTopBar = false,
                onBackButtonClick = navController::popBackStack
            )
        }
        searchSection()
        userSection(
            onBackButton = navController::popBackStack
        )
    }
}

fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false