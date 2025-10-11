package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

/**
 *  Data class that represents the step of the create wizard:
 *
 *  **FIRST** - First step in which we ask for the name of the Cocktail and if it's alcoholic or not
 *  **SECOND** - Second step in which we ask for type, method and glass used
 *  **THIRD** - Third step in which we ask for the ingredients and their quantity
 *  **FOURTH** - Fourth step in which we ask for the instructions
 *  **FIFTH** - Fifth step in which we ask for the image of the Cocktail
 *  **UPLOADING** - Step in which we upload the Cocktail to the server
 *  **SUCCESS** - Final step in which we confirm the creation and upload it to db
 */
sealed class CreateWizardStepData(
    val order: Int,
    val icon: ImageVector,
    var isSelected: Boolean = false
) {
    data object First : CreateWizardStepData(0, Icons.Filled.Menu, true)
    data object Second : CreateWizardStepData(1, Icons.Filled.Info)
    data object Third : CreateWizardStepData(2, Icons.Filled.Build)
    data object Fourth : CreateWizardStepData(3, Icons.Filled.AddCircle)
    data object Fifth : CreateWizardStepData(4, Icons.Filled.AccountCircle)
    data object Uploading : CreateWizardStepData(100, Icons.Filled.Refresh)
    data object Success : CreateWizardStepData(5, Icons.Filled.Check)
}

@Composable
fun CreateWizardStepBar(
    modifier: Modifier = Modifier,
    steps: List<CreateWizardStepData> =
        CreateWizardStepData::class.sealedSubclasses.mapNotNull { it.objectInstance },
    unWantedSteps: List<CreateWizardStepData> = listOf(
        CreateWizardStepData.Uploading
    ),
    currentStep: CreateWizardStepData = CreateWizardStepData.Third,
    animationDuration: Int = 500
) {
    val currentStepIndex = currentStep.order
    val mappedSteps = steps
        .map { createWizardStepData ->
            createWizardStepData.also { it.isSelected = it.order == currentStepIndex }
        }
        .filterOutUnwantedSteps(unWantedSteps)
        .sortedBy { it.order }

    Row(
        modifier = modifier
            .speakEazyGradientBackground()
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(mappedSteps) { index, createWizardStepData ->
                val isFirst = index == 0
                val isLast = index == mappedSteps.lastIndex
                val isCompleted = index < currentStepIndex
                val isCurrent = index == currentStepIndex

                val padding =
                    if (isFirst && isCurrent) 4.dp else if (isLast && isCurrent) 12.dp else 0.dp

                val paddingModifier = when {
                    isFirst -> Modifier.padding(start = padding)
                    isLast -> Modifier.padding(end = padding)
                    else -> Modifier
                }

                val progressBar by animateFloatAsState(
                    targetValue = when {
                        isCompleted -> 1f // Step già completato → barra piena
                        isCurrent -> 1f   // Step attuale → si riempie
                        else -> 0f        // Futuro → vuoto
                    },
                    animationSpec = tween(durationMillis = animationDuration),
                    label = "barProgress"
                )

                val progressStep by animateFloatAsState(
                    targetValue = when {
                        isCompleted -> 1f // Step già completato → barra piena
                        isCurrent -> 1f   // Step attuale → si riempie
                        else -> 0f        // Futuro → vuoto
                    },
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = 250
                    ),
                    label = "stepProgress"
                )

                if (index > 0) {
                    val barColor = if (isCurrent) {
                        lerp(Color.DarkGray, YellowFFE271, progressBar)
                    } else {
                        if (index <= currentStepIndex) YellowFFE271 else Color.DarkGray
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillParentMaxWidth(fraction = 1f / (mappedSteps.size + 4)), // magic number
                        thickness = 4.dp,
                        color = barColor
                    )
                }

                val stepColor = if (isCurrent) {
                    lerp(Color.DarkGray, YellowFFE271, progressStep)
                } else {
                    if (index <= currentStepIndex) YellowFFE271 else Color.DarkGray
                }

                Row(
                    modifier = paddingModifier
                        .graphicsLayer {
                            val scale = if (createWizardStepData.isSelected) 1.5f else 1f
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(CircleShape)
                        .size(24.dp)
                        .background(stepColor)
                        .zIndex(10f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        visible = createWizardStepData.isSelected,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Icon(
                            modifier = Modifier.padding(3.dp),
                            imageVector = createWizardStepData.icon,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateWizardLinearStepBar(
    modifier: Modifier = Modifier,
    steps: List<CreateWizardStepData> =
        CreateWizardStepData::class.sealedSubclasses.mapNotNull { it.objectInstance },
    unWantedSteps: List<CreateWizardStepData> = listOf(
        CreateWizardStepData.Uploading
    ),
    currentStep: CreateWizardStepData = CreateWizardStepData.Third,
    animationDuration: Int = 500
) {
    val currentStepIndex = currentStep.order
    val mappedSteps = steps
        .map { createWizardStepData ->
            createWizardStepData.also { it.isSelected = it.order == currentStepIndex }
        }
        .filterOutUnwantedSteps(unWantedSteps)
        .sortedBy { it.order }

    Row(
        modifier = modifier
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.Absolute.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(mappedSteps) { index, createWizardStepData ->
                val isCompleted = index < currentStepIndex
                val isCurrent = index == currentStepIndex

                val progressBar by animateFloatAsState(
                    targetValue = when {
                        isCompleted -> 1f // Step già completato → barra piena
                        isCurrent -> 1f   // Step attuale → si riempie
                        else -> 0f        // Futuro → vuoto
                    },
                    animationSpec = tween(durationMillis = animationDuration),
                    label = "barProgress"
                )

                val barColor = if (isCurrent) {
                    lerp(Color.DarkGray, YellowFFE271, progressBar)
                } else {
                    if (index <= currentStepIndex)
                        YellowFFE271.withAlpha(0.8f)
                    else
                        Color.DarkGray
                }

                HorizontalDivider(
                    modifier = Modifier
                        .graphicsLayer {
                            val scale = if (isCurrent) 1.6f else 1f
                            scaleY = scale
                        }
                        .fillParentMaxWidth(fraction = (1f / mappedSteps.size) - 0.005f) // magic number
                        .clip(
                            RoundedCornerShape(
                                if (createWizardStepData.isSelected) 8.dp else 2.dp
                            )
                        )
                        .zIndex(10f),
                    thickness = 5.dp,
                    color = barColor
                )
            }
        }
    }
}

private fun List<CreateWizardStepData>.filterOutUnwantedSteps(
    unWantedSteps: List<CreateWizardStepData>
) = this.filter { currentStep -> unWantedSteps.all { it != currentStep } }

@Preview
@Composable
fun CreateWizardStepBarPreview(
    @PreviewParameter(CreateWizardParameterProvider::class) currentStep: CreateWizardStepData
) {
    CreateWizardStepBar(
        currentStep = currentStep
    )
}

@Preview
@Composable
fun OtherStepBarPreview(
    @PreviewParameter(CreateWizardParameterProvider::class) currentStep: CreateWizardStepData
) {
    CreateWizardLinearStepBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp),
        currentStep = currentStep
    )
}

private class CreateWizardParameterProvider : PreviewParameterProvider<CreateWizardStepData> {
    override val values: Sequence<CreateWizardStepData> = sequenceOf(
        CreateWizardStepData.First,
        CreateWizardStepData.Second,
        CreateWizardStepData.Third,
        CreateWizardStepData.Fourth,
        CreateWizardStepData.Fifth,
        CreateWizardStepData.Success

    )
}