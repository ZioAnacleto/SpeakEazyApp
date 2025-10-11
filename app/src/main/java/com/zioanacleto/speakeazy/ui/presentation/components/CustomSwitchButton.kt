package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun CustomSwitchButton(
    modifier: Modifier = Modifier,
    switchPadding: Dp = 2.dp,
    buttonHeight: Dp,
    buttonWidth: Dp = (2 * buttonHeight),
    firstValue: Boolean = false,
    animationDuration: Int = 700,
    onContent: @Composable () -> Unit = {},
    offContent: @Composable () -> Unit = {},
    onValueChange: (Boolean) -> Unit = {}
) {

    val switchSize by remember {
        mutableStateOf(buttonHeight - switchPadding * 2)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var switchClicked by remember {
        mutableStateOf(firstValue)
    }

    var padding by remember {
        mutableStateOf(0.dp)
    }

    padding = if (switchClicked)
        buttonWidth - switchSize - switchPadding * 2
    else
        0.dp

    val animateSize by animateDpAsState(
        targetValue = if (switchClicked) padding else 0.dp,
        tween(
            durationMillis = animationDuration,
            easing = LinearOutSlowInEasing
        )
    )

    val progressBar by animateFloatAsState(
        targetValue = if(switchClicked) 1f else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "barProgress"
    )

    val barColor = lerp(Color.DarkGray, YellowFFE271, progressBar)

    Box(
        modifier = modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .clip(CircleShape)
            .background(barColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                switchClicked = !switchClicked
                onValueChange(switchClicked)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(switchPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animateSize)
                    .background(Color.Transparent)
            )

            Box(
                modifier = Modifier
                    .size(switchSize)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = switchClicked,
                    animationSpec = tween(durationMillis = animationDuration)
                ) {
                    if(it) onContent() else offContent()
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomSwitchButtonPreview() {
    CustomSwitchButton(
        buttonHeight = 50.dp,
        firstValue = false,
        onContent = {
            Icon(
                rememberVectorPainter(Icons.TwoTone.AddCircle),
                contentDescription = null
            )
        },
        offContent = {
            Icon(
                rememberVectorPainter(Icons.TwoTone.Build),
                contentDescription = null
            )
        }
    )
}