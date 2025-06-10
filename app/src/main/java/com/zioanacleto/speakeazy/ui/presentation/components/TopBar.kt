package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.SpeakEazyAppState
import com.zioanacleto.speakeazy.navigation.TopBarDestination

@Composable
fun TopBar(
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
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        appState = SpeakEazyAppState(
            navController = rememberNavController()
        )
    )
}