package com.zioanacleto.speakeazy.ui.presentation.instructions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.Cocktail3DScene
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.domain.Cocktail3DSceneController
import com.zioanacleto.speakeazy.ui.presentation.components.BackFloatingButton
import com.zioanacleto.speakeazy.ui.presentation.components.bottomSheetStyle
import com.zioanacleto.speakeazy.ui.presentation.components.speakEazyGradientBackground
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.InstructionModel
import org.koin.androidx.compose.get

@Composable
fun InstructionsScreen(
    modifier: Modifier = Modifier,
    instructions: List<InstructionModel>,
    onBackButton: () -> Unit
) {
    InstructionsScreenContent(modifier, instructions, onBackButton = onBackButton)
}

@Composable
private fun InstructionsScreenContent(
    modifier: Modifier = Modifier,
    instructions: List<InstructionModel>,
    controller: Cocktail3DSceneController = get(),
    onBackButton: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 26.dp, start = 16.dp),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        items(instructions, key = { it.instruction }) { item ->
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 30.dp)
                                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                                    .clickable { controller.updateCurrentScene(item.type) }
                                    .padding(8.dp),
                                text = item.instruction,
                                color = Color.White,
                                fontSize = TextUnit(18f, TextUnitType.Sp)
                            )
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