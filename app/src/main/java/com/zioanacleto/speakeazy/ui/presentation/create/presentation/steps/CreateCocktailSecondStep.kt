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
import androidx.compose.ui.res.stringResource
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
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun CreateCocktailSecondStepScreen(
    modifier: Modifier = Modifier,
    cocktailType: String = "",
    cocktailMethod: String = "",
    cocktailGlass: String = "",
    onPreviousButtonClick: () -> Unit,
    onNextButtonClick: (String, String, String) -> Unit
) {
    var cocktailTypeTextState by remember {
        mutableStateOf(TextFieldValue(cocktailType))
    }
    var cocktailMethodTextState by remember {
        mutableStateOf(TextFieldValue(cocktailMethod))
    }
    var cocktailGlassTextState by remember {
        mutableStateOf(TextFieldValue(cocktailGlass))
    }

    val isContinueButtonEnabled = cocktailTypeTextState.text.isNotEmpty()
            && cocktailMethodTextState.text.isNotEmpty()
            && cocktailGlassTextState.text.isNotEmpty()

    Column(
        modifier = modifier
            .hideKeyboardOnTouch(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Type
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            text = stringResource(R.string.create_cocktail_second_step__first_question_title),
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            value = cocktailTypeTextState,
            onValueChange = { mail ->
                cocktailTypeTextState = mail
            },
            label = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_second_step__first_question_label)
                )
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_second_step__first_question_placeholder)
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(14f, TextUnitType.Sp)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                showKeyboardOnFocus = true,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowFFE271,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = YellowFFE271
            )
        )

        // Method
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            text = stringResource(R.string.create_cocktail_second_step__second_question_title),
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            value = cocktailMethodTextState,
            onValueChange = { mail ->
                cocktailMethodTextState = mail
            },
            label = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_second_step__second_question_label)
                )
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_second_step__second_question_placeholder)
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(14f, TextUnitType.Sp)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                showKeyboardOnFocus = true,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowFFE271,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = YellowFFE271
            )
        )

        // Glass
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            text = stringResource(R.string.create_cocktail_second_step__third_question_title),
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            value = cocktailGlassTextState,
            onValueChange = { mail ->
                cocktailGlassTextState = mail
            },
            label = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_second_step__third_question_label)
                )
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_second_step__third_question_placeholder)
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(14f, TextUnitType.Sp)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
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
                Text(text = stringResource(R.string.create_cocktail_second_step__back_button))
            }

            Button(
                enabled = isContinueButtonEnabled,
                onClick = {
                    onNextButtonClick(
                        cocktailTypeTextState.text,
                        cocktailMethodTextState.text,
                        cocktailGlassTextState.text,
                    )
                }
            ) {
                Text(text = stringResource(R.string.create_cocktail_second_step__continue_button))
            }
        }
    }
}

@Preview
@Composable
fun CreateCocktailSecondStepScreenPreview() {
    CreateCocktailSecondStepScreen(
        cocktailType = "test",
        cocktailMethod = "test",
        cocktailGlass = "test",
        onPreviousButtonClick = {},
        onNextButtonClick = { _, _, _ -> }
    )
}