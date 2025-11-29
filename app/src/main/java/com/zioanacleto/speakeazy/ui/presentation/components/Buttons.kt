package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun BackFloatingButton(
    modifier: Modifier = Modifier,
    delayTime: Long = 200L,
    onBackButton: () -> Unit
) {
    var enabled = rememberEnabledForButton(delayTime)

    FloatingActionButton(
        modifier = modifier
            .padding(start = 16.dp, top = 40.dp)
            .size(50.dp)
            .clip(CircleShape),
        backgroundColor = Color.Black.withAlpha(),
        contentColor = Color.White,
        onClick = {
            if (enabled) onBackButton()
            enabled = !enabled
        }
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
            contentDescription = "Back Button"
        )
    }
}

@Composable
fun SafeClickableGenericButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    delayTime: Long = 200L,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    var buttonEnabled = rememberEnabledForButton(delayTime)

    Button(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        onClick = {
            if (buttonEnabled) onClick()
            buttonEnabled = !buttonEnabled
        }
    ) {
        content()
    }
}