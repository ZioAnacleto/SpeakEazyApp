package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.SpeakEazyAppState
import com.zioanacleto.speakeazy.navigation.TopBarDestination

@Composable
fun MainTopBar(
    modifier: Modifier = Modifier,
    appState: SpeakEazyAppState
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp, end = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            onClick = {
                appState.navigateToTopBarDestination(TopBarDestination.USER_SETTINGS)
            }
        ) {
            Image(
                painter = painterResource(R.drawable.default_user_image),
                contentDescription = TopBarDestination.USER_SETTINGS.pageName
            )
        }
    }
}

@Composable
fun CreateCocktailTopBar(
    modifier: Modifier = Modifier,
    appState: SpeakEazyAppState,
    title: String = "Create"
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp, start = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(50.dp)
                .clip(CircleShape),
            backgroundColor = Color.Transparent,
            contentColor = Color.White,
            onClick = {
                appState.navController.popBackStack()
            }
        ) {
            Image(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            color = Color.White,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun MainTopBarPreview() {
    MainTopBar(
        appState = SpeakEazyAppState(
            navController = rememberNavController(),
            isUserLogged = false
        )
    )
}

@Preview
@Composable
fun CreateCocktailBarPreview(modifier: Modifier = Modifier) {
    CreateCocktailTopBar(
        appState = SpeakEazyAppState(
            navController = rememberNavController(),
            isUserLogged = false
        )
    )
}