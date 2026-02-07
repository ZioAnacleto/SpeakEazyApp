package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zioanacleto.speakeazy.R

@Composable
fun OfflineBlockingScreen(
    modifier: Modifier = Modifier,
    @RawRes rawAnimation: Int = R.raw.no_wifi_animation,
    cancellationBehavior: LottieCancellationBehavior = LottieCancellationBehavior.Immediately
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val lottieComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(rawAnimation)
        )

        val progress by animateLottieCompositionAsState(
            lottieComposition,
            iterations = LottieConstants.IterateForever,
            cancellationBehavior = cancellationBehavior
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = lottieComposition,
                progress = progress,
                modifier = Modifier.size(260.dp),
                enableMergePaths = true,
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier.padding(20.dp),
                text = "No internet connection detected, please check and try again later.",
                fontSize = TextUnit(24f, TextUnitType.Sp),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview
@Composable
private fun OfflineBlockingScreenPreview() {
    OfflineBlockingScreen()
}