package com.zioanacleto.speakeazy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.FavoritesBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.FavoritesRoute
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.MainBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.MainRoute
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.SearchBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.SearchRoute
import kotlin.reflect.KClass

enum class BottomBarDestination(
    val pageName: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: KClass<*>,
    val baseRoute: KClass<*>
) {
    HOME(
        pageName = "All cocktails",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = MainRoute::class,
        baseRoute = MainBaseRoute::class
    ),
    FAVORITES(
        pageName = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Filled.FavoriteBorder,
        route = FavoritesRoute::class,
        baseRoute = FavoritesBaseRoute::class
    ),
    SEARCH(
        pageName = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        route = SearchRoute::class,
        baseRoute = SearchBaseRoute::class
    )
}