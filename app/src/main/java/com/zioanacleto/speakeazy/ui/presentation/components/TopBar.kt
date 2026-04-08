package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.navigation.TopBarDestination

@Composable
fun MainTopBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
            onClick = onClick
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
    title: String = stringResource(R.string.topbar__create_title),
    onClick: () -> Unit
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
            onClick = onClick
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
    MainTopBar {}
}

@Preview
@Composable
fun CreateCocktailBarPreview() {
    CreateCocktailTopBar { }
}