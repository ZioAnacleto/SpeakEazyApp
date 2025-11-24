package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zioanacleto.speakeazy.domain.CREATE_COCKTAIL_MAX_INGREDIENTS_NUMBER
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.CreateCocktailNewIngredientDialog
import com.zioanacleto.speakeazy.ui.presentation.components.GlassWithIngredients
import com.zioanacleto.speakeazy.ui.presentation.components.IngredientLayer
import com.zioanacleto.speakeazy.ui.presentation.components.SafeClickableGenericButton
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.CreateCocktailIngredientsUiState
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.CreateCocktailViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CreateCocktailThirdStep(
    modifier: Modifier = Modifier,
    ingredients: MutableMap<String, String> = mutableMapOf(), // map of <id, measure>
    onPreviousButtonClick: () -> Unit,
    onButtonClick: (Map<String, String>) -> Unit
) {
    val viewModel = getViewModel<CreateCocktailViewModel>()
    val ingredientsUiState = viewModel.ingredientsUiState.collectAsStateWithLifecycle()

    val maxIngredients = CREATE_COCKTAIL_MAX_INGREDIENTS_NUMBER
    val missingIngredientsList by rememberSaveable {
        mutableStateOf(ingredients)
    }
    var dialogStartIndex by remember { mutableIntStateOf(0) }
    var dialogStartMeasure by remember { mutableStateOf("") }
    var isEditModeDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    when (ingredientsUiState.value) {
        is CreateCocktailIngredientsUiState.Success -> {
            val ingredientsList =
                (ingredientsUiState.value as CreateCocktailIngredientsUiState.Success)
                    .ingredients.ingredients.map { it.name to it.id }

            var counter by remember { mutableIntStateOf(ingredients.size) }

            val uiIngredients = missingIngredientsList.keys.map {
                IngredientLayer(
                    id = ingredientsList.first { remote -> remote.first == it }.first,
                    name = it,
                    measure = ingredients[it] ?: "",
                    color = Color(0xFFB5651D), // todo: generate colors randomly
                    ratio = 1f / ingredients.size
                )
            }

            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SafeClickableGenericButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    enabled = counter < maxIngredients,
                    onClick = {
                        showDialog = !showDialog
                    }
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Filled.Add),
                        contentDescription = "Add ingredient"
                    )
                }

                Box(
                    modifier = Modifier
                        .size(300.dp, 450.dp)
                        .padding(horizontal = 20.dp, vertical = 30.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    GlassWithIngredients(
                        ingredients = uiIngredients,
                        onDeleteItemClick = {

                        }
                    ) { selectedIngredient ->
                        dialogStartIndex =
                            ingredientsList.indexOf(
                                ingredientsList.firstOrNull { it.first == selectedIngredient.id }
                            )
                        dialogStartMeasure = selectedIngredient.measure
                        isEditModeDialog = !isEditModeDialog
                        showDialog = !showDialog
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
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
                        onClick = {
                            val list =
                                (ingredientsUiState.value as CreateCocktailIngredientsUiState.Success)
                                    .ingredients.ingredients

                            onButtonClick(
                                ingredients.map { (ingredient, value) ->
                                    list.first { it.name == ingredient }.id to value
                                }.toMap()
                            )
                        }
                    ) {
                        Text(text = "Continue")
                    }
                }
            }

            if (showDialog) {
                CreateCocktailNewIngredientDialog(
                    ingredientsList = ingredientsList,
                    startIndex = dialogStartIndex,
                    selectedMeasure = dialogStartMeasure,
                    onConfirmButtonClick = {
                        missingIngredientsList[it.first] = it.second
                        if (!isEditModeDialog) counter++
                        isEditModeDialog = !isEditModeDialog
                        showDialog = !showDialog
                    },
                    onDismissButtonClick = { showDialog = !showDialog }
                )
            }
        }

        is CreateCocktailIngredientsUiState.Error -> {
            // todo: show error
        }

        is CreateCocktailIngredientsUiState.Loading -> {
            CocktailLoadingAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            )
        }
    }
}

@Preview
@Composable
fun CreateCocktailThirdStepScreenNewPreview() {
    CreateCocktailThirdStep(
        onPreviousButtonClick = {},
        onButtonClick = {}
    )
}