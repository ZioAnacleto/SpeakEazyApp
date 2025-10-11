package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.speakeazy.domain.CREATE_COCKTAIL_MAX_INGREDIENTS_NUMBER
import com.zioanacleto.speakeazy.domain.CREATE_COCKTAIL_MEASURES_LIST
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.CreateCocktailIngredientComponent
import com.zioanacleto.speakeazy.ui.presentation.components.SafeClickableGenericButton
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.CreateCocktailIngredientsUiState
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.CreateCocktailViewModel
import org.koin.androidx.compose.getViewModel

// todo: check if everything is ok here
@Composable
fun CreateCocktailThirdStepScreen(
    modifier: Modifier = Modifier,
    ingredients: MutableMap<String, String> = mutableMapOf(),
    onPreviousButtonClick: () -> Unit,
    onButtonClick: (Map<String, String>) -> Unit
) {
    val viewModel = getViewModel<CreateCocktailViewModel>()
    val ingredientsUiState = viewModel.ingredientsUiState.collectAsStateWithLifecycle()

    val maxIngredients = CREATE_COCKTAIL_MAX_INGREDIENTS_NUMBER
    val missingIngredientsList = remember<MutableList<String>> { mutableStateListOf() }

    when (ingredientsUiState.value) {
        is CreateCocktailIngredientsUiState.Success -> {
            val ingredientsList =
                (ingredientsUiState.value as CreateCocktailIngredientsUiState.Success)
                    .ingredients.ingredients.map { it.name }

            var counter by remember { mutableIntStateOf(ingredients.keys.size) }

            LazyColumn(
                modifier = modifier
                    .hideKeyboardOnTouch()
            ) {
                // fixed ingredients
                if (ingredients.isNotEmpty()) {
                    items(
                        items = ingredients.keys.toList(),
                        key = { it }
                    ) { givenIngredient ->
                        val (measureValue, measureSelected) =
                            ingredients[givenIngredient]?.split(" ")?.take(2) ?: listOf("", "")

                        CreateCocktailIngredientComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(98.dp),
                            ingredientSelected = givenIngredient,
                            measureValueSelected = measureValue,
                            measureSelected = measureSelected,
                            showConfirmButton = false,
                            ingredients = ingredientsList,
                            measures = CREATE_COCKTAIL_MEASURES_LIST,
                            onConfirm = { ingredient, measure ->
                                val ingredientId =
                                    (ingredientsUiState.value as CreateCocktailIngredientsUiState.Success)
                                        .ingredients.ingredients.first { it.name == ingredient }.id
                                ingredients[ingredientId] = measure
                            }
                        )
                    }
                }

                // dynamic ingredients
                items(
                    items = missingIngredientsList,
                    key = { it }
                ) {
                    CreateCocktailIngredientComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(98.dp)
                            .animateItem(),
                        ingredients = ingredientsList,
                        measures = CREATE_COCKTAIL_MEASURES_LIST,
                        onConfirm = { ingredient, measure ->
                            ingredients[ingredient] = measure
                        }
                    )
                }

                item {
                    SafeClickableGenericButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        enabled = (ingredients.keys.size + missingIngredientsList.size) < maxIngredients,
                        onClick = {
                            if (missingIngredientsList.isNotEmpty())
                                missingIngredientsList.removeAt(0)
                            missingIngredientsList.add("item $counter")
                            counter++
                        }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Filled.Add),
                            contentDescription = "Add ingredient"
                        )
                    }
                }

                item {
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
fun CreateCocktailThirdStepScreenPreview() {
    CreateCocktailThirdStepScreen(
        onPreviousButtonClick = {},
        onButtonClick = {}
    )
}