package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

// todo: we can create something better
@Composable
fun CreateCocktailIngredientComponent(
    modifier: Modifier = Modifier,
    ingredientSelected: String = "",
    measureValueSelected: String = "",
    measureSelected: String = "cl",
    showConfirmButton: Boolean = true,
    ingredients: List<String>,
    measures: List<String>,
    onConfirm: (String, String) -> Unit
) {
    val ingredientMutableInteractionSource = remember { MutableInteractionSource() }
    val ingredientsTapped by ingredientMutableInteractionSource.collectIsFocusedAsState()

    var ingredientsExpanded by remember { mutableStateOf(false) }
    var measuresExpanded by remember { mutableStateOf(false) }

    var selectedIngredient by remember { mutableStateOf(ingredientSelected) }
    var measureTextState by remember { mutableStateOf(TextFieldValue(measureValueSelected)) }
    var selectedMeasure by remember { mutableStateOf(measureSelected) }

    var isConfirmButtonVisible by remember { mutableStateOf(showConfirmButton) }
    var isConfirmButtonEnabled by remember { mutableStateOf(false) }
    val ingredientComponentFraction = if (isConfirmButtonVisible) 0.92f else 1f

    val measureValueRegex = Regex("^\\d+(\\.\\d+)?\$")
    var isMeasureValueError by remember { mutableStateOf(false) }

    LaunchedEffect(ingredientsTapped) {
        ingredientsExpanded = ingredientsTapped
    }

    Box(
        modifier = modifier
            .hideKeyboardOnTouch()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = ingredientComponentFraction)
                .padding(top = 15.dp, bottom = 15.dp, start = 4.dp, end = 11.dp)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .border(
                    width = 2.dp,
                    color = YellowFFE271,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            TextField(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(fraction = 0.5f),
                value = selectedIngredient,
                onValueChange = { selectedIngredient = it },
                readOnly = true,
                label = {
                    Text(
                        color = Color.White,
                        text = "Ingredient"
                    )
                },
                interactionSource = ingredientMutableInteractionSource,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.White
                )
            )

            DropdownMenu(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(fraction = 0.5f),
                expanded = ingredientsExpanded && ingredientsTapped,
                onDismissRequest = { ingredientsExpanded = !ingredientsExpanded },
                properties = PopupProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                )
            ) {
                ingredients.forEach { ingredient ->
                    DropdownMenuItem(
                        text = {
                            Text(text = ingredient)
                        },
                        onClick = {
                            selectedIngredient = ingredient
                            ingredientsExpanded = !ingredientsExpanded
                            isConfirmButtonEnabled =
                                selectedIngredient.isNotEmpty() && measureTextState.text.isNotEmpty()
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    thickness = 2.dp,
                    color = YellowFFE271
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.7f)
                        .background(Color.Transparent),
                    value = measureTextState,
                    onValueChange = {
                        measureTextState = it
                        isMeasureValueError = !measureValueRegex.matches(it.text)
                        isConfirmButtonEnabled =
                            selectedIngredient.isNotEmpty() &&
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
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = YellowFFE271,
                        textColor = Color.White
                    )
                )

                Text(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .clickable {
                            measuresExpanded = !measuresExpanded
                        },
                    color = Color.White,
                    text = selectedMeasure,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )

                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.132f)
                        .align(Alignment.Bottom),
                    expanded = measuresExpanded,
                    onDismissRequest = { measuresExpanded = !measuresExpanded },
                    properties = PopupProperties(
                        dismissOnClickOutside = true,
                        dismissOnBackPress = true
                    )
                ) {
                    measures.forEach { measure ->
                        DropdownMenuItem(
                            text = {
                                Text(text = measure)
                            },
                            onClick = {
                                selectedMeasure = measure
                                measuresExpanded = !measuresExpanded
                            }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.1f)
                .padding(start = 2.dp, end = 4.dp)
                .align(Alignment.CenterEnd),
            visible = isConfirmButtonVisible
        ) {
            IconButton(
                enabled = isConfirmButtonEnabled,
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = Color.White,
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.White.withAlpha(0.5f)
                ),
                onClick = {
                    isConfirmButtonVisible = !isConfirmButtonVisible
                    onConfirm(selectedIngredient, "${measureTextState.text} $selectedMeasure")
                }
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.Filled.CheckCircle),
                    contentDescription = null,
                    tint = if (isConfirmButtonEnabled) Color.White else Color.White.withAlpha(0.5f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCocktailIngredientComponentPreview() {
    val ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3")
    val measures = listOf("Measure 1", "Measure 2", "Measure 3")
    CreateCocktailIngredientComponent(
        modifier = Modifier
            .fillMaxWidth()
            .height(98.dp)
            .background(Color.DarkGray),
        ingredients = ingredients,
        measures = measures,
        onConfirm = { _, _ -> }
    )
}