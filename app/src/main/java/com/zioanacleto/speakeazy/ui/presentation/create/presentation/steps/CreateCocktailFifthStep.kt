package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.speakeazy.R

// todo: to be implemented all logic to upload a picture
@Composable
fun CreateCocktailFifthStepScreen(
    modifier: Modifier = Modifier,
    onPreviousButtonClick: () -> Unit,
    onButtonClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .hideKeyboardOnTouch(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 10.dp)
        ) {
            Button(
                modifier = Modifier
                    .padding(end = 10.dp),
                shape = ButtonDefaults.outlinedShape,
                onClick = onPreviousButtonClick
            ) {
                Text(text = stringResource(R.string.create_cocktail_fifth_step__back_button))
            }

            Button(
                onClick = { onButtonClick("") }
            ) {
                Text(text = stringResource(R.string.create_cocktail_fifth_step__continue_button))
            }
        }
    }
}

@Preview
@Composable
fun CreateCocktailFifthStepScreenPreview() {
    CreateCocktailFifthStepScreen(
        onPreviousButtonClick = {},
        onButtonClick = {}
    )
}