package com.zioanacleto.speakeazy.ui.presentation.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.zioanacleto.speakeazy.USER_DEEPLINK_URI
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserScreen
import kotlinx.serialization.Serializable

@Serializable
data object UserRoute

@Serializable
data object UserBaseRoute

fun NavController.navigateToUser(navOptions: NavOptions) =
    navigate(route = UserRoute, navOptions = navOptions)

fun NavGraphBuilder.userSection(
    onBackButton: () -> Unit
) {
    navigation<UserBaseRoute>(startDestination = UserRoute) {
        composable<UserRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = USER_DEEPLINK_URI
                }
            )
        ) {
            UserScreen(
                onBackButton = onBackButton
            )
        }
    }
}