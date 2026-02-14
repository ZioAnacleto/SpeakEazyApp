package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.rememberAsyncImagePainter
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.domain.main.model.BannerModel
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun BannerSection(
    modifier: Modifier = Modifier,
    banner: BannerModel,
    onCtaClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.Black),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .zIndex(10f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = banner.name,
                color = Color.White,
                fontSize = TextUnit(24f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 30.dp)
                    .border(2.dp, YellowFFE271, RoundedCornerShape(8.dp))
                    .clickable { onCtaClick(banner.cocktail.id) }
                    .padding(8.dp),
                text = banner.cta.default(),
                color = YellowFFE271,
                fontSize = TextUnit(18f, TextUnitType.Sp)
            )
        }
        Image(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 200.dp)
                .fillMaxSize(),
            painter = rememberAsyncImagePainter(
                model = banner.cocktail.imageUrl
            ),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BannerSectionPreview() {
    val bannerModel = BannerModel(
        name = "Banner Example",
        position = "1",
        cta = "Click me",
        cocktail = DrinkModel(
            id = "1",
            name = "Example",
            category = "Example",
            imageUrl = "link"
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        BannerSection(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
                .height(200.dp),
            banner = bannerModel
        ) { }
    }
}