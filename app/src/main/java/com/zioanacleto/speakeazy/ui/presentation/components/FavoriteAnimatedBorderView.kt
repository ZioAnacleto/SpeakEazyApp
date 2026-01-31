package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GradientCircularShadowBox(
    modifier: Modifier = Modifier,
    color: Color,
    initialHaloBorderWidth: Dp,
    pressedHaloBorderWidth: Dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable (BoxScope.() -> Unit)
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedSpread by animateDpAsState(
        targetValue = if (isPressed) pressedHaloBorderWidth else initialHaloBorderWidth,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            //animated halo shadow
            .drawOutlineCircularShadowGradient(
                color = color.copy(alpha = 0.6f),
                haloBorderWidth = animatedSpread,
            )
            .then(
                if (isPressed) {
                    //add optionally small lighting ring
                    Modifier.drawOutlineCircularShadowGradient(
                        color = color,
                        haloBorderWidth = 4.dp,
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null  //disable ripple
            ) {
                onClick()
            }
    ) { content() }
}

private fun Modifier.drawOutlineCircularShadowGradient(
    color: Color,
    haloBorderWidth: Dp,
) = this
    .drawBehind {
        val realSizePx = size.minDimension
        val haloBorderWidthPx = haloBorderWidth.toPx()
        val innerRadius = realSizePx / 2
        val totalRadius = innerRadius + haloBorderWidthPx

        // Create a radial gradient with color stops
        val gradientBrush = Brush.radialGradient(
            0.0f to color.copy(alpha = 0.0f), // Starting at the center with 0.0 alpha
            innerRadius / totalRadius to color.copy(alpha = 0.0f), //just to make inner circle transparent
            innerRadius / totalRadius to color, // At the edge of the inner circle
            1f to color.copy(alpha = 0f), // At the outer edge with 0 alpha
            center = center,
            radius = totalRadius,
            tileMode = TileMode.Clamp
        )

        drawCircle(
            brush = gradientBrush,
            radius = totalRadius,
            center = center
        )
    }

@Preview
@Composable
fun GradientCircularShadowBoxPreview() {
    val mis = remember { MutableInteractionSource() }
    Box(Modifier.size(200.dp)) {
        GradientCircularShadowBox(
            modifier = Modifier
                .padding(end = 16.dp, top = 40.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Black.withAlpha()),
            color = Color.White,
            initialHaloBorderWidth = 0.dp,
            pressedHaloBorderWidth = 48.dp,
            interactionSource = mis,
            onClick = {},
        ) {
            BackFloatingButton { }
        }
    }
}