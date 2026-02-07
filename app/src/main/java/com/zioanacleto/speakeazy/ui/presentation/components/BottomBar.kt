package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination
import com.zioanacleto.speakeazy.navigation.BottomBarDestination
import com.zioanacleto.speakeazy.navigation.bottomBarAdditionalDestinations
import com.zioanacleto.speakeazy.navigation.bottomBarDestinations
import com.zioanacleto.speakeazy.navigation.isRouteInHierarchy
import com.zioanacleto.speakeazy.ui.theme.BottomBarBackground

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    isUserLogged: Boolean,
    currentDestination: NavDestination?,
    animateCreateButton: Boolean = true,
    onDestinationChanged: (BottomBarDestination) -> Unit
) {
    if (isUserLogged)
        BottomBarLogged(modifier, currentDestination, animateCreateButton, onDestinationChanged)
    else
        BottomBarNotLogged(modifier, currentDestination, onDestinationChanged)
}

// todo: change this structure in order to correct animation
@Composable
private fun BottomBarLogged(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    animateCreateButton: Boolean,
    onDestinationChanged: (BottomBarDestination) -> Unit
) {
    val fractionForItems = (3f / 4)
    val fractionForSingleItem = (1f / 4.1f)
    val roundedCornerBig = 20.dp
    val roundedCornerSmall = 4.dp

    var animate by remember { mutableStateOf(!animateCreateButton) }

    LaunchedEffect(Unit) { animate = !animate }

    LazyRow(
        modifier = modifier
            .height(56.dp)
            .shadow(elevation = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            val mainFraction by animateFloatAsState(
                targetValue = if (animate) fractionForItems else 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
                label = "mainFraction"
            )

            Row(
                modifier = Modifier
                    .zIndex(1f)
                    .fillParentMaxWidth(fraction = mainFraction)
                    .clip(
                        RoundedCornerShape(
                            topStart = roundedCornerBig,
                            bottomStart = roundedCornerBig,
                            topEnd = roundedCornerSmall,
                            bottomEnd = roundedCornerSmall
                        )
                    )
                    .background(color = BottomBarBackground.withAlpha(0.92f))
                    .animateItem(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarNavigationItems(
                    currentDestination,
                    bottomBarDestinations,
                    onDestinationChanged
                )
            }
        }
        item {
            val translation by animateFloatAsState(
                targetValue = if (animate) 0f else -120f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing,
                    delayMillis = 500
                ),
                label = "translation"
            )

            Row(
                modifier = Modifier
                    .fillParentMaxWidth(fraction = fractionForSingleItem)
                    .zIndex(0f)
                    .graphicsLayer {
                        translationX = translation
                    }
                    .clip(
                        RoundedCornerShape(
                            topStart = roundedCornerSmall,
                            bottomStart = roundedCornerSmall,
                            topEnd = roundedCornerBig,
                            bottomEnd = roundedCornerBig
                        )
                    )
                    .background(color = BottomBarBackground.withAlpha(0.92f))
                    .animateItem()
            ) {
                BottomBarNavigationItems(
                    currentDestination,
                    bottomBarAdditionalDestinations,
                    onDestinationChanged
                )
            }
        }
    }
}

@Composable
private fun BottomBarNotLogged(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    onDestinationChanged: (BottomBarDestination) -> Unit
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
    ) { BottomBarNavigationItems(currentDestination, bottomBarDestinations, onDestinationChanged) }
}

@Composable
private fun RowScope.BottomBarNavigationItems(
    currentDestination: NavDestination?,
    destinationList: List<BottomBarDestination>,
    onDestinationChanged: (BottomBarDestination) -> Unit
) {
    destinationList.forEach { destination ->
        val isSelected = currentDestination.isRouteInHierarchy(destination.baseRoute)

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
                onClick = { onDestinationChanged(this) }
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
        isUserLogged = isUserLogged,
        currentDestination = null
    ) {}
}

class BooleanParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}