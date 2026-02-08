package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp

fun Modifier.speakEazyGradientBackground() =
    this.background(
        Brush.horizontalGradient(
            colors = listOf(
                Color(12, 11, 24, 255),
                Color(27, 14, 36, 255),
                Color(41, 20, 51, 255)
            )
        )
    )

fun Modifier.speakEazyInvertedGradientBackground() =
    this.background(
        Brush.horizontalGradient(
            colors = listOf(
                Color(30, 23, 39, 255),
                Color(42, 28, 51, 255),
                Color(46, 28, 58, 255)
            )
        )
    )

fun Modifier.parallaxLayoutModifier(scrollState: ScrollState, rate: Int) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val height = if (rate > 0) scrollState.value / rate else scrollState.value
        layout(placeable.width, placeable.height) {
            placeable.place(0, height)
        }
    }

fun Color.withAlpha(alpha: Float = 0.75f): Color =
    this.copy(alpha = alpha)

fun Modifier.bottomSheetStyle() =
    graphicsLayer {
        translationY = (-16).dp.toPx()
    }
        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))