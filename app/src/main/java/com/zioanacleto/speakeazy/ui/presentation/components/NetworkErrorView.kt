package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zioanacleto.speakeazy.R

@Composable
fun NetworkErrorView(
    modifier: Modifier = Modifier,
    errorText: String = stringResource(R.string.network_error_view__error)
) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.generic_error_animation)
    )

    val progress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = 1,
        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = progress,
            modifier = Modifier.size(260.dp),
            contentScale = ContentScale.FillBounds
        )

        Text(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
            text = errorText,
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun NetworkErrorViewPreview() {
    NetworkErrorView()
}