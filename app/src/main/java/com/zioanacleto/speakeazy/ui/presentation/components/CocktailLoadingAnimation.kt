package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zioanacleto.speakeazy.R

@Composable
fun CocktailLoadingAnimation(
    modifier: Modifier = Modifier,
    @RawRes rawAnimation: Int = R.raw.loading_animation,
    cancellationBehavior: LottieCancellationBehavior = LottieCancellationBehavior.Immediately
) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawAnimation)
    )

    val progress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
        cancellationBehavior = cancellationBehavior
    )

    LottieAnimation(
        composition = lottieComposition,
        progress = progress,
        modifier = modifier,
        enableMergePaths = true
    )
}