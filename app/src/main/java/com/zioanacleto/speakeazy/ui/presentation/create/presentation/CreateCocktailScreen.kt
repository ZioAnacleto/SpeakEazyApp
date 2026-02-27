package com.zioanacleto.speakeazy.ui.presentation.create.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.ConfirmationDialog
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardLinearStepBar
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import com.zioanacleto.speakeazy.ui.presentation.components.CrossSlide
import com.zioanacleto.speakeazy.ui.presentation.components.fromOrderToStep
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailDisambiguationScreen
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailFifthStepScreen
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailFirstStepScreen
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailFourthStepScreen
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailSecondStepScreen
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailSuccessStepScreen
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.steps.CreateCocktailThirdStep
import com.zioanacleto.speakeazy.ui.theme.LocalSnackBarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateCocktailScreen(
    modifier: Modifier = Modifier,
    onCloseButtonClick: () -> Unit
) {
    CreateCocktailScreenContent(
        modifier,
        onCloseButtonClick
    )
}

@Composable
private fun CreateCocktailScreenContent(
    modifier: Modifier = Modifier,
    onCloseButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 14.dp, end = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val viewModel: CreateCocktailViewModel = koinViewModel()
        val currentUiState by viewModel.createCocktailUiState.collectAsState()

        var isForwardScreen by remember { mutableStateOf(true) }

        val createCocktailToBeDeleted by viewModel.createCocktailToBeDeleted.collectAsState()
        val isCreateCocktailDeleted by viewModel.createCocktailDeleted.collectAsState()
        val isCreateCocktailUploaded by viewModel.createCocktailUploaded.collectAsState()
        val snackbarHostState = LocalSnackBarHostState.current

        createCocktailToBeDeleted?.let {
            ConfirmationDialog(
                title = "Delete",
                message = "Are you sure you want to delete this cocktail wizard?",
                confirmButtonText = "Delete",
                dismissButtonText = "Cancel",
                onConfirmButtonClick = {
                    viewModel.deleteCreateCocktail(it.id)
                },
                onDismissButtonClick = {
                    viewModel.abortDeleteCreateCocktail()
                }
            )
        }

        LaunchedEffect(isCreateCocktailDeleted) {
            if (isCreateCocktailDeleted) {
                snackbarHostState.showSnackbar("Cocktail wizard deleted.")
            }
        }

        LaunchedEffect(currentUiState) {
            if ((currentUiState as? CreateCocktailUiState.SuccessSingle)
                    ?.createCocktail?.currentStep == CreateWizardStepData.Uploading.order
            )
                viewModel.uploadCurrentCocktail()
        }

        LaunchedEffect(isCreateCocktailUploaded) {
            if (isCreateCocktailUploaded)
                viewModel.saveSuccessStep()
        }

        when (currentUiState) {
            is CreateCocktailUiState.SuccessMultiple -> {
                CreateCocktailDisambiguationScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    createCocktailModels =
                        (currentUiState as CreateCocktailUiState.SuccessMultiple).createCocktails,
                    onCreateCocktailSelected = { selectedCocktail ->
                        viewModel.selectCreateCocktail(selectedCocktail)
                    },
                    onDeleteCocktailSelected = { selectedCocktail ->
                        viewModel.setCreateCocktailToBeDeleted(selectedCocktail)
                    },
                    onAddWizard = {
                        viewModel.createNewWizard()
                    }
                )
            }

            is CreateCocktailUiState.SuccessSingle -> {
                val currentState = currentUiState as CreateCocktailUiState.SuccessSingle
                val currentStep = currentState.createCocktail.currentStep
                CreateWizardLinearStepBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 10.dp, end = 10.dp),
                    currentStep = currentStep.fromOrderToStep()
                )

                CrossSlide(
                    targetState = currentStep.fromOrderToStep(),
                    reverseAnimation = !isForwardScreen
                ) { it: CreateWizardStepData ->
                    when (it) {
                        CreateWizardStepData.First -> {
                            CreateCocktailFirstStepScreen(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                cocktailName = currentState.createCocktail.cocktailName.default(),
                                isAlcoholic = currentState.createCocktail.isAlcoholic,
                            ) { cocktailName, isAlcoholic ->
                                isForwardScreen = true
                                viewModel.saveFirstStep(
                                    cocktailName,
                                    isAlcoholic
                                )
                            }
                        }

                        CreateWizardStepData.Second -> {
                            CreateCocktailSecondStepScreen(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                cocktailType = currentState.createCocktail.type.default(),
                                cocktailMethod = currentState.createCocktail.method.default(),
                                cocktailGlass = currentState.createCocktail.glass.default(),
                                onPreviousButtonClick = {
                                    isForwardScreen = false
                                    viewModel.toPreviousStep(CreateWizardStepData.First)
                                }
                            ) { type, method, glass ->
                                isForwardScreen = true
                                viewModel.saveSecondStep(type, method, glass)
                            }
                        }

                        CreateWizardStepData.Third -> {
                            CreateCocktailThirdStep(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                ingredients = currentState.createCocktail.ingredients.toMutableMap(),
                                onPreviousButtonClick = {
                                    isForwardScreen = false
                                    viewModel.toPreviousStep(CreateWizardStepData.Second)
                                }
                            ) { ingredients ->
                                isForwardScreen = true
                                viewModel.saveThirdStep(ingredients)
                            }
                        }

                        CreateWizardStepData.Fourth -> {
                            CreateCocktailFourthStepScreen(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                cocktailInstructions = currentState.createCocktail.instructions.default(),
                                onPreviousButtonClick = {
                                    isForwardScreen = false
                                    viewModel.toPreviousStep(CreateWizardStepData.Third)
                                }
                            ) { instructions ->
                                isForwardScreen = true
                                viewModel.saveFourthStep(instructions)
                            }
                        }

                        CreateWizardStepData.Fifth -> {
                            CreateCocktailFifthStepScreen(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                onPreviousButtonClick = {
                                    isForwardScreen = false
                                    viewModel.toPreviousStep(CreateWizardStepData.Fourth)
                                }
                            ) { imageUrl ->
                                isForwardScreen = true
                                viewModel.saveFifthStep(imageUrl)
                            }
                        }

                        CreateWizardStepData.Uploading -> {
                            // todo: search a new animation for uploading phase
                            CocktailLoadingAnimation(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(40.dp)
                            )
                        }

                        CreateWizardStepData.Success -> {
                            CreateCocktailSuccessStepScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 20.dp),
                                onButtonClick = {
                                    viewModel.deleteCreateCocktail()
                                    onCloseButtonClick()
                                }
                            )
                        }
                    }
                }
            }

            is CreateCocktailUiState.Error -> {
                // this should not happen here
            }

            is CreateCocktailUiState.Loading -> {
                // todo
            }
        }
    }
}