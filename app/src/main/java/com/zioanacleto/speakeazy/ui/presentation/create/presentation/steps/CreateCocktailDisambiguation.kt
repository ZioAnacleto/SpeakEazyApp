package com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.components.CreateCocktailInformation
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import com.zioanacleto.speakeazy.ui.presentation.components.fromOrderToStep
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import java.util.Date
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateCocktailDisambiguationScreen(
    modifier: Modifier = Modifier,
    createCocktailModels: List<CreateCocktailModel>,
    iconsList: List<Int> = listOf(
        R.drawable.create_cocktail_wizard,
        R.drawable.create_cocktail_cauldron,
        R.drawable.create_cocktail_spellbook,
        R.drawable.create_cocktail_witch_hat
    ),
    onCreateCocktailSelected: (CreateCocktailModel) -> Unit,
    onDeleteCocktailSelected: (CreateCocktailModel) -> Unit,
    onAddWizard: () -> Unit
) {
    Box(modifier = modifier) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                text = "Choose a cocktail wizard to complete",
                color = Color.White,
                fontSize = TextUnit(24f, TextUnitType.Sp)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp),
                text = "Long tap on an element to delete it.",
                color = Color.White,
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )

            if(createCocktailModels.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(
                        createCocktailModels,
                        key = { it.id.default() }
                    ) { item ->
                        val iconIndex = Random.nextInt(iconsList.size)

                        CreateCocktailInformation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = { onCreateCocktailSelected(item) },
                                    onLongClick = { onDeleteCocktailSelected(item) }
                                )
                                .padding(10.dp)
                                .animateItem(),
                            cocktailName = item.cocktailName.default(),
                            image = iconsList[iconIndex],
                            createdDate = item.createdTime.toLocaleString(),
                            currentStep = item.currentStep.fromOrderToStep()
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                    }
                }
            }
            else {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No wizards here, create a new one.",
                        color = Color.White,
                        fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(end = 10.dp, bottom = 20.dp)
                .align(Alignment.BottomEnd),
            backgroundColor = YellowFFE271,
            onClick = onAddWizard
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Filled.Add),
                contentDescription = "Create cocktail"
            )
        }
    }
}

@Preview
@Composable
fun CreateCocktailDisambiguationScreenEmptyListPreview() {
    CreateCocktailDisambiguationScreen(
        createCocktailModels = listOf(),
        onCreateCocktailSelected = {},
        onDeleteCocktailSelected = {},
        onAddWizard = {}
    )
}

@Preview
@Composable
fun CreateCocktailDisambiguationScreenPreview() {
    CreateCocktailDisambiguationScreen(
        createCocktailModels = listOf(
            CreateCocktailModel(
                cocktailName = "test1",
                createdTime = Date(),
                lastUpdateTime = Date(),
                currentStep = CreateWizardStepData.First.order
            ),
            CreateCocktailModel(
                cocktailName = "test2",
                createdTime = Date(),
                lastUpdateTime = Date(),
                currentStep = CreateWizardStepData.Third.order
            ),
            CreateCocktailModel(
                cocktailName = "test3",
                createdTime = Date(),
                lastUpdateTime = Date(),
                currentStep = CreateWizardStepData.First.order
            ),
            CreateCocktailModel(
                cocktailName = "test4 lungo lungo per andare su due righe",
                createdTime = Date(),
                lastUpdateTime = Date(),
                currentStep = CreateWizardStepData.Third.order
            )
        ),
        onCreateCocktailSelected = {},
        onDeleteCocktailSelected = {},
        onAddWizard = {}
    )
}