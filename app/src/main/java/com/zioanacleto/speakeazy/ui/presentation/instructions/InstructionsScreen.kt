package com.zioanacleto.speakeazy.ui.presentation.instructions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.core.domain.main.model.InstructionModel
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.Cocktail3DScene
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller.Cocktail3DSceneController
import com.zioanacleto.speakeazy.ui.presentation.components.BackFloatingButton
import com.zioanacleto.speakeazy.ui.presentation.components.InstructionListItem
import com.zioanacleto.speakeazy.ui.presentation.components.bottomSheetStyle
import com.zioanacleto.speakeazy.ui.presentation.components.speakEazyGradientBackground
import org.koin.androidx.compose.get

@Composable
fun InstructionsScreen(
    modifier: Modifier = Modifier,
    cocktailName: String,
    glassType: String,
    instructions: List<InstructionModel>,
    onBackButton: () -> Unit
) {
    InstructionsScreenContent(
        modifier,
        cocktailName,
        glassType,
        instructions,
        onBackButton = onBackButton
    )
}

@Composable
private fun InstructionsScreenContent(
    modifier: Modifier = Modifier,
    cocktailName: String,
    glassType: String,
    instructions: List<InstructionModel>,
    controller: Cocktail3DSceneController = get(),
    onBackButton: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    controller.updateCurrentModel(glassType to "red")
    controller.updateCurrentScene(instructions.first().type)

    var selectedInstruction by remember { mutableStateOf(instructions.first()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .speakEazyGradientBackground()
        ) {
            // 3D cocktail scene
            Cocktail3DScene(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { startAnimation = it }

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .bottomSheetStyle()
                        .speakEazyGradientBackground(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 26.dp, start = 16.dp),
                        fontSize = TextUnit(32f, TextUnitType.Sp),
                        color = Color.White,
                        text = cocktailName
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 16.dp),
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        color = Color.White,
                        text = "Directions"
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, start = 16.dp, end = 16.dp)
                    ) {
                        itemsIndexed(
                            instructions,
                            key = { _, item -> item.instruction }
                        ) { index, item ->
                            InstructionListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 1.dp),
                                isSelected = item == selectedInstruction,
                                isLastItem = index == instructions.lastIndex,
                                isFirstItem = index == 0,
                                instructionModel = item
                            ) {
                                selectedInstruction = item
                                controller.updateCurrentScene(item.type)
                            }
                        }
                    }
                }
            }
        }

        BackFloatingButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            onBackButton = onBackButton
        )
    }
}