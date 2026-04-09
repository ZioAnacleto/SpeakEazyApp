package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun CocktailDetailInformation(
    modifier: Modifier = Modifier,
    glassTitle: String = stringResource(R.string.cocktail_detail_information__glass_title),
    cocktailTypeTitle: String = stringResource(R.string.cocktail_detail_information__type_title),
    methodTitle: String = stringResource(R.string.cocktail_detail_information__method_title),
    glassType: String,
    cocktailType: String,
    method: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .widthIn(10.dp, 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = Color.White,
                text = glassTitle.uppercase(),
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                color = YellowFFE271,
                text = glassType,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(18f, TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        }

        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = Color.LightGray
        )

        Column(
            modifier = Modifier
                .widthIn(10.dp, 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = Color.White,
                text = cocktailTypeTitle.uppercase(),
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                color = YellowFFE271,
                text = cocktailType,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(18f, TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        }

        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = Color.LightGray
        )

        Column(
            modifier = Modifier
                .widthIn(10.dp, 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = Color.White,
                text = methodTitle.uppercase(),
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                color = YellowFFE271,
                text = method,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(18f, TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun CocktailDetailInformationPreview() {
    CocktailDetailInformation(
        glassType = "Long text to test",
        cocktailType = "Summer but also winter",
        method = "Shake & Strain"
    )
}