package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.theme.Pink80
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun NewsBanner(
    modifier: Modifier = Modifier,
    text: String
) {
    Card(
        modifier = modifier
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            YellowFFE271,
                            Pink80
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = text,
                fontSize = TextUnit(18f, TextUnitType.Sp),
                color = Color(41, 20, 51, 255),
                fontWeight = FontWeight.Bold
            )
        }
    }
}