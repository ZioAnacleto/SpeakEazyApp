package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.R
import kotlin.math.roundToInt

@Composable
fun CreateCocktailInformation(
    modifier: Modifier = Modifier,
    cocktailName: String,
    createdDate: String,
    @DrawableRes image: Int,
    currentStep: CreateWizardStepData
) {
    val currentProgress: CreateWizardStepData.() -> Float = {
        val totalSteps = CreateWizardStepData::class.sealedSubclasses.size
        val currentStepIndex = currentStep.order
        (currentStepIndex + 1) / totalSteps.toFloat()
    }

    Card(
        modifier = modifier
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.DarkGray,
        elevation = 6.dp
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .align(Alignment.TopStart)
                    .widthIn(0.dp, 160.dp)
                    .padding(end = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    color = Color.White,
                    text = cocktailName,
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    text = stringResource(
                        R.string.create_cocktail_information__created_date_text,
                        createdDate
                    ),
                    color = Color.White,
                )
            }

            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.75f
            )

            Box(
                modifier = Modifier
                    .padding(14.dp)
                    .size(50.dp)
                    .align(Alignment.BottomStart)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(),
                    progress = currentStep.currentProgress(),
                    color = Color.White,
                    backgroundColor = Color.LightGray.withAlpha(alpha = 0.2f),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 6.dp
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "${(currentStep.currentProgress() * 100).roundToInt()}%",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
fun CreateCocktailInformationPreview() {
    CreateCocktailInformation(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        cocktailName = "Mojito lungo lungo lungo che più lungo non si può ma che forse si può",
        image = R.drawable.create_cocktail_cauldron,
        createdDate = "Today",
        currentStep = CreateWizardStepData.Second
    )
}