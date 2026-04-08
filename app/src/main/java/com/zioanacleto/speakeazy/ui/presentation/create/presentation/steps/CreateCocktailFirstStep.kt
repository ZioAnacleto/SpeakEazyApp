package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
fun CreateCocktailFirstStepScreen(
    modifier: Modifier = Modifier,
    cocktailName: String = "",
    isAlcoholic: Boolean = false,
    onButtonClick: (String, Boolean) -> Unit
) {
    var cocktailNameTextState by remember {
        mutableStateOf(TextFieldValue(cocktailName))
    }
    var selected by remember { mutableStateOf(isAlcoholic) }

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
            text = stringResource(R.string.create_cocktail_first_step__first_question_title),
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            value = cocktailNameTextState,
            onValueChange = { mail ->
                cocktailNameTextState = mail
            },
            label = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_first_step__first_question_label)
                )
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.create_cocktail_first_step__first_question_placeholder)
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(14f, TextUnitType.Sp)
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                showKeyboardOnFocus = true
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowFFE271,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = YellowFFE271
            )
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            text = stringResource(R.string.create_cocktail_first_step__second_question_title),
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )

        FilterChip(
            modifier = Modifier
                .padding(top = 10.dp),
            onClick = { selected = !selected },
            label = {
                Text(stringResource(R.string.create_cocktail_first_step__second_question_alcoholic))
            },
            selected = selected,
            leadingIcon = if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            },
        )

        Button(
            modifier = modifier
                .padding(bottom = 10.dp, top = 20.dp),
            enabled = cocktailNameTextState.text.isNotEmpty(),
            onClick = { onButtonClick(cocktailNameTextState.text, selected) }
        ) {
            Text(text = stringResource(R.string.create_cocktail_first_step__continue_button))
        }
    }
}

@Preview
@Composable
fun CreateCocktailFirstStepScreenPreview() {
    CreateCocktailFirstStepScreen(
        cocktailName = "test",
        isAlcoholic = true,
        onButtonClick = { _, _ -> }
    )
}