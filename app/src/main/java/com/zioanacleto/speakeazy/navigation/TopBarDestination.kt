package com.zioanacleto.speakeazy.navigation

import com.zioanacleto.speakeazy.ui.presentation.user.navigation.UserBaseRoute
import com.zioanacleto.speakeazy.ui.presentation.user.navigation.UserRoute
import kotlin.reflect.KClass

enum class TopBarDestination(
    val pageName: String,
    val route: KClass<*>,
    val baseRoute: KClass<*>
) {
    USER_SETTINGS(
        pageName = "User Settings",
        route = UserRoute::class,
        baseRoute = UserBaseRoute::class
    )
}