package com.zioanacleto.speakeazy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.zioanacleto.speakeazy.core.analytics.NetworkMonitor
import com.zioanacleto.speakeazy.navigation.BottomBarDestination
import com.zioanacleto.speakeazy.navigation.TopBarDestination
import com.zioanacleto.speakeazy.navigation.bottomBarAllDestinations
import com.zioanacleto.speakeazy.ui.presentation.create.navigation.navigateToCreate
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.navigateToFavorites
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.navigateToMain
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.navigateToSearch
import com.zioanacleto.speakeazy.ui.presentation.user.navigation.navigateToUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberSpeakEazyAppState(
    navController: NavHostController = rememberNavController(),
    isUserLogged: Boolean = false,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(
    navController,
    isUserLogged,
    networkMonitor,
    coroutineScope
) {
    SpeakEazyAppState(navController, isUserLogged, networkMonitor, coroutineScope)
}

@Stable
class SpeakEazyAppState(
    val navController: NavHostController,
    val isUserLogged: Boolean,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope,
    var animateCreateButton: Boolean = true
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
        @Composable get() {
            return bottomBarAllDestinations.firstOrNull { destination ->
                currentDestination?.hasRoute(destination.route) == true
            }
        }

    val showBottomBar: Boolean
        @Composable get() {
            return currentBottomBarDestination != null &&
                    currentBottomBarDestination != BottomBarDestination.CreateCocktail
        }

    // checks if device is online or not
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun navigateToBottomBarDestination(
        destination: BottomBarDestination,
        navOptions: NavOptions = navOptions { }
    ) {
        when (destination) {
            is BottomBarDestination.Home ->
                navController.navigateToMain(navOptions)

            is BottomBarDestination.Search ->
                navController.navigateToSearch(navOptions)

            is BottomBarDestination.Favorites ->
                navController.navigateToFavorites(navOptions)

            is BottomBarDestination.CreateCocktail ->
                navController.navigateToCreate(navOptions)
        }
    }

    fun navigateToTopBarDestination(
        destination: TopBarDestination,
        navOptions: NavOptions = navOptions {  }
    ) {
        when (destination) {
            TopBarDestination.USER_SETTINGS -> {
                navController.navigateToUser(navOptions)
            }
        }
    }
}