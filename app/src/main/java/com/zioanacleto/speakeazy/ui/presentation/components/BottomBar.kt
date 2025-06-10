package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.SpeakEazyAppState
import com.zioanacleto.speakeazy.navigation.BottomBarDestination
import com.zioanacleto.speakeazy.navigation.isRouteInHierarchy
import com.zioanacleto.speakeazy.ui.theme.BottomBarBackground
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun BottomBar(
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
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val isSelected = appState.currentDestination.isRouteInHierarchy(destination.baseRoute)

            BottomNavigationItem(
                selected = isSelected,
                icon = {
                    Icon(
                        if (isSelected)
                            destination.selectedIcon
                        else
                            destination.unselectedIcon,
                        contentDescription = destination.pageName,
                        tint = if (isSelected) YellowFFE271 else Color.LightGray
                    )
                },
                onClick = {
                    appState.navigateToBottomBarDestination(destination)
                }
            )
        }
    }
}