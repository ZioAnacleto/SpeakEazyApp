package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun CreateCocktailFourthStepScreen(
    modifier: Modifier = Modifier,
    cocktailInstructions: String = "",
    onPreviousButtonClick: () -> Unit,
    onButtonClick: (String) -> Unit
) {
    var cocktailInstructionsTextState by remember {
        mutableStateOf(TextFieldValue(cocktailInstructions))
    }

    Column(
        modifier = modifier
            .hideKeyboardOnTouch(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            text = "How's your cocktail made?",
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            value = cocktailInstructionsTextState,
            onValueChange = { mail ->
                cocktailInstructionsTextState = mail
            },
            label = {
                Text(
                    color = Color.White,
                    text = "Instructions"
                )
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = "Insert cocktail's instructions"
                )
            },
            singleLine = false,
            minLines = 6,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(14f, TextUnitType.Sp)
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                showKeyboardOnFocus = true,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowFFE271,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = YellowFFE271
            )
        )

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
                Text(text = "Back")
            }

            Button(
                enabled = cocktailInstructionsTextState.text.isNotEmpty(),
                onClick = { onButtonClick(cocktailInstructionsTextState.text) }
            ) {
                Text(text = "Continue")
            }
        }
    }
}

@Preview
@Composable
fun CreateCocktailFourthStepScreenPreview() {
    CreateCocktailFourthStepScreen(
        cocktailInstructions = "Pour some sugar on me",
        onPreviousButtonClick = {},
        onButtonClick = {}
    )
}