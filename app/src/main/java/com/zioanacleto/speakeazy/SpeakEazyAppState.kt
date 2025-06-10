package com.zioanacleto.speakeazy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.zioanacleto.speakeazy.navigation.BottomBarDestination
import com.zioanacleto.speakeazy.navigation.TopBarDestination
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.navigateToFavorites
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.navigateToMain
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.navigateToSearch
import com.zioanacleto.speakeazy.ui.presentation.user.navigation.navigateToUser

@Composable
fun rememberSpeakEazyAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    SpeakEazyAppState(navController)
}

@Stable
class SpeakEazyAppState(
    val navController: NavHostController
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentBottomBarDestination: BottomBarDestination?
        @Composable get(){
            return BottomBarDestination.entries.firstOrNull { destination ->
                currentDestination?.hasRoute(destination.route) == true
            }
        }

    fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        val navOptions = navOptions { }
        when (destination) {
            BottomBarDestination.HOME ->
                navController.navigateToMain(navOptions)

            BottomBarDestination.SEARCH -> {
                navController.navigateToSearch(navOptions)
            }
            BottomBarDestination.FAVORITES -> {
                navController.navigateToFavorites(navOptions)
            }
        }
    }

    fun navigateToTopBarDestination(destination: TopBarDestination) {
        val navOptions = navOptions { }
        when(destination) {
            TopBarDestination.USER_SETTINGS -> {
                navController.navigateToUser(navOptions)
            }
        }
    }
}