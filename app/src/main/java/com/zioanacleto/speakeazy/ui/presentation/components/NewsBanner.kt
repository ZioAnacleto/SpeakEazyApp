package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.theme.Pink80
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun NewsBanner(
    modifier: Modifier = Modifier,
    text: String,
    timed: Boolean = false
) {
    var showBanner by remember { mutableStateOf(true) }
    val progress = remember { Animatable(1f) }

    if (timed) {
        LaunchedEffect(Unit) {
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(5000)
            )
            showBanner = false
        }
    }

    FadeAndSlideAnimatedVisibility(showBanner) {
        Card(
            modifier = modifier
                .padding(16.dp),
            elevation = CardDefaults.elevatedCardElevation()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

                if (timed)
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .height(5.dp),
                        progress = { progress.value },
                        strokeCap = StrokeCap.Round,
                        color = Color(41, 20, 51, 255),
                        trackColor = Color.Transparent,
                        drawStopIndicator = {}
                    )
            }
        }
    }
}

@Preview
@Composable
private fun NewsBannerPreview() {
    NewsBanner(
        modifier = Modifier,
        text = "Testo di test giusto per vedere il risultato di questa bella roba",
        timed = true
    )
}