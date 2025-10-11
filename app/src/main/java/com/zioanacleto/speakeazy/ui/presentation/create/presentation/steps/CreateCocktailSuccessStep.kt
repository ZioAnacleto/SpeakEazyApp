package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.presentation.components.SafeClickableGenericButton

// todo: beautify
@Composable
fun CreateCocktailSuccessStepScreen(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cocktail uploaded successfully!",
                color = Color.White,
                fontSize = TextUnit(34f, TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        }

        SafeClickableGenericButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            enabled = true,
            onClick = onButtonClick
        ) {
            Text(
                text = "Close",
                color = Color.White,
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )
        }
    }
}

@Preview
@Composable
fun CreateCocktailSuccessStepScreenPreview() {
    CreateCocktailSuccessStepScreen(
        modifier = Modifier.fillMaxSize(),
        onButtonClick = {}
    )
}