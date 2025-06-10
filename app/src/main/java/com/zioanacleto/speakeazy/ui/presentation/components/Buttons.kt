package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonElevation
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun BackFloatingButton(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier
            .padding(start = 16.dp, top = 40.dp)
            .size(50.dp)
            .clip(CircleShape),
        backgroundColor = Color.Black.withAlpha(),
        contentColor = Color.White,
        onClick = onBackButton
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
            contentDescription = "Back Button"
        )
    }
}