package com.zioanacleto.speakeazy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.zioanacleto.speakeazy.ui.presentation.create.navigation.CreateBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.create.navigation.CreateRoute
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.FavoritesBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.favorites.navigation.FavoritesRoute
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.MainBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.main.navigation.MainRoute
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.SearchBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.search.navigation.SearchRoute
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import kotlin.reflect.KClass

sealed class BottomBarDestination(
    val pageName: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val selectedColor: Color,
    val unselectedColor: Color,
    val route: KClass<*>,
    val baseRoute: KClass<*>
) {
    data object Home : BottomBarDestination(
        pageName = "All cocktails",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        selectedColor = YellowFFE271,
        unselectedColor = Color.LightGray,
        route = MainRoute::class,
        baseRoute = MainBaseRoute::class
    )
    data object Favorites : BottomBarDestination(
        pageName = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Filled.FavoriteBorder,
        selectedColor = YellowFFE271,
        unselectedColor = Color.LightGray,
        route = FavoritesRoute::class,
        baseRoute = FavoritesBaseRoute::class
    )
    data object Search : BottomBarDestination(
        pageName = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        selectedColor = YellowFFE271,
        unselectedColor = Color.LightGray,
        route = SearchRoute::class,
        baseRoute = SearchBaseRoute::class
    )
    data object CreateCocktail : BottomBarDestination(
        pageName = "Create",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Filled.Add,
        selectedColor = Color.LightGray,
        unselectedColor = Color.LightGray,
        route = CreateRoute::class,
        baseRoute = CreateBaseRoute::class
    )
}

val bottomBarAllDestinations = BottomBarDestination::class.sealedSubclasses
    .mapNotNull { it.objectInstance }

val bottomBarDestinations = listOf(
    BottomBarDestination.Home,
    BottomBarDestination.Favorites,
    BottomBarDestination.Search
)

val bottomBarAdditionalDestinations = listOf(
    BottomBarDestination.CreateCocktail
)