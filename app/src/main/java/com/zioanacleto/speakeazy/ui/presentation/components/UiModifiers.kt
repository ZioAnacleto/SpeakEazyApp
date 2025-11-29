package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import kotlinx.coroutines.delay

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

@Composable
fun rememberEnabledForButton(
    delayTime: Long = 200L
): Boolean {
    var enabled by remember { mutableStateOf(true) }
    LaunchedEffect(enabled) {
        if(enabled) return@LaunchedEffect else delay(delayTime)
        enabled = true
    }

    return enabled
}