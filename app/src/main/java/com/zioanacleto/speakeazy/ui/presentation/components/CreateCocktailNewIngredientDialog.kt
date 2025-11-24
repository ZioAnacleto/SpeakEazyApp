package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

// todo: for now we return just the pair (name, id), change it with correct implementation
@Composable
fun CreateCocktailNewIngredientDialog(
    modifier: Modifier = Modifier,
    ingredientsList: List<Pair<String, String>>,
    startIndex: Int = 0,
    selectedMeasure: String = "",
    onConfirmButtonClick: (Pair<String, String>) -> Unit,
    onDismissButtonClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissButtonClick,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        var currentIngredient = ingredientsList[startIndex].first

        var measureTextState by remember { mutableStateOf(TextFieldValue(selectedMeasure)) }
        var isConfirmButtonEnabled by remember { mutableStateOf(false) }
        val measureValueRegex = Regex("^\\d+(\\.\\d+)?\$")
        var isMeasureValueError by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .size(width = 280.dp, height = 350.dp)
                .background(Color.Black.withAlpha(0.8f))
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                .hideKeyboardOnTouch(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CircularCarouselWithAnimations(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(200.dp),
                list = ingredientsList.map { it.first },
                startIndex = startIndex,
                orientation = CircularCarouselOrientation.VERTICAL,
                padding = 70.dp,
                currentItem = { currentItem ->
                    currentIngredient = currentItem
                }
            ) {
                CircularCarouselIngredientItem(
                    ingredient = it,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(horizontal = 20.dp),
                    value = measureTextState,
                    onValueChange = {
                        measureTextState = it
                        isMeasureValueError = !measureValueRegex.matches(it.text)
                        isConfirmButtonEnabled =
                            measureTextState.text.isNotEmpty() &&
                                    !isMeasureValueError
                    },
                    label = {
                        Text(
                            color = Color.White,
                            text = "Measure"
                        )
                    },
                    isError = isMeasureValueError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YellowFFE271,
                        unfocusedBorderColor = Color.DarkGray,
                        cursorColor = YellowFFE271,
                        focusedTextColor = Color.White
                    )
                )
            }

            SafeClickableGenericButton(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp),
                enabled = isConfirmButtonEnabled,
                onClick = {
                    AnacletoLogger.mumbling(
                        mumble = "Current ingredient: $currentIngredient, measure: ${measureTextState.text}cl"
                    )
                    onConfirmButtonClick(
                        currentIngredient to "${measureTextState.text} cl"
                    )
                }
            ) {
                Text(
                    text = "Add",
                    color = Color.White,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCocktailNewIngredientDialogPreview() {
    val list = listOf(
        "Brandy" to "1",
        "Tequila" to "2",
        "Lemon Juice" to "3",
        "Coke" to "4",
        "Sugar" to "5",
        "Gin" to "6",
        "Mint" to "7",
        "Cocoa Cream" to "8",
    )

    CreateCocktailNewIngredientDialog(
        ingredientsList = list,
        startIndex = 4,
        onConfirmButtonClick = { _ -> }
    ) { }
}