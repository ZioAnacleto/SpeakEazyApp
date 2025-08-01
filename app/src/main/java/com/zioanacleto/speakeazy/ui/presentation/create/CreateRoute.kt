package com.zioanacleto.speakeazy.ui.presentation.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
data object CreateBaseRoute

@Serializable
data object CreateRoute

fun NavController.navigateToCreate(
    navOptions: NavOptions? = null
) = navigate(route = CreateRoute, navOptions = navOptions)

fun NavGraphBuilder.createSection(
    onBackButtonClick: () -> Unit
) {
    navigation<CreateBaseRoute>(startDestination = CreateRoute) {
        composable<CreateRoute> {
            // todo add create screen
        }
    }
}