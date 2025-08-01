package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.SpeakEazyAppState
import com.zioanacleto.speakeazy.navigation.BottomBarDestination
import com.zioanacleto.speakeazy.navigation.bottomBarAdditionalDestinations
import com.zioanacleto.speakeazy.navigation.bottomBarDestinations
import com.zioanacleto.speakeazy.navigation.isRouteInHierarchy
import com.zioanacleto.speakeazy.rememberSpeakEazyAppState
import com.zioanacleto.speakeazy.ui.theme.BottomBarBackground

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    appState: SpeakEazyAppState
) {
    if (appState.isUserLogged)
        BottomBarLogged(modifier, appState)
    else
        BottomBarNotLogged(modifier, appState)
}

@Composable
private fun BottomBarLogged(
    modifier: Modifier = Modifier,
    appState: SpeakEazyAppState
) {
    val fractionForItems = (3f / 4)
    val fractionForSingleItem = (1f / 4.1f)
    val roundedCornerBig = 20.dp
    val roundedCornerSmall = 4.dp

    LazyRow(
        modifier = modifier
            .height(56.dp)
            .shadow(elevation = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth(fraction = fractionForItems)
                    .clip(
                        RoundedCornerShape(
                            topStart = roundedCornerBig,
                            bottomStart = roundedCornerBig,
                            topEnd = roundedCornerSmall,
                            bottomEnd = roundedCornerSmall
                        )
                    )
                    .background(color = BottomBarBackground.withAlpha(0.92f)),
                verticalAlignment = Alignment.CenterVertically
            ) { BottomBarNavigationItems(appState, bottomBarDestinations) }
        }
        item {
            AnimatedVisibility(
                appState.animateCreateButton,
                enter = slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillParentMaxWidth(fraction = fractionForSingleItem)
                        .clip(
                            RoundedCornerShape(
                                topStart = roundedCornerSmall,
                                bottomStart = roundedCornerSmall,
                                topEnd = roundedCornerBig,
                                bottomEnd = roundedCornerBig
                            )
                        )
                        .background(color = BottomBarBackground.withAlpha(0.92f))
                ) { BottomBarNavigationItems(appState, bottomBarAdditionalDestinations) }
            }
        }
    }
}

@Composable
private fun BottomBarNotLogged(
    modifier: Modifier = Modifier,
    appState: SpeakEazyAppState
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(color = BottomBarBackground.withAlpha(0.92f))
            .shadow(elevation = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) { BottomBarNavigationItems(appState, bottomBarDestinations) }
}

@Composable
private fun RowScope.BottomBarNavigationItems(
    appState: SpeakEazyAppState,
    destinationList: List<BottomBarDestination>
) {
    destinationList.forEach { destination ->
        val isSelected = appState.currentDestination.isRouteInHierarchy(destination.baseRoute)

        with(destination) {
            BottomNavigationItem(
                selected = isSelected,
                icon = {
                    Icon(
                        if (isSelected)
                            selectedIcon
                        else
                            unselectedIcon,
                        contentDescription = pageName,
                        tint = if (isSelected) selectedColor else unselectedColor
                    )
                },
                onClick = {
                    appState.navigateToBottomBarDestination(this)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview(
    @PreviewParameter(BooleanParameterProvider::class) isUserLogged: Boolean
) {
    BottomBar(
        modifier = Modifier.padding(horizontal = 50.dp),
        appState = rememberSpeakEazyAppState(isUserLogged = isUserLogged)
    )
}

class BooleanParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}