package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.zioanacleto.buffa.compose.safeClickable

@Composable
fun MainDrinkCard(
    modifier: Modifier,
    id: String,
    name: String,
    category: String,
    imageString: String,
    isFavorite: Boolean,
    onClick: (String) -> Unit = {}
) {
    Card(
        modifier = modifier
            .size(160.dp, 220.dp)
            .safeClickable { onClick(id) },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.DarkGray,
        elevation = 6.dp
    ) {
        val painter = rememberAsyncImagePainter(
            model = imageString
        )
        Box(
            modifier = Modifier
                .paint(
                    painter = painter,
                    contentScale = ContentScale.Crop
                )
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .widthIn(0.dp, 120.dp)
                    .padding(end = 20.dp),
                color = Color.White,
                text = name
            )
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd),
                painter = if(isFavorite)
                    rememberVectorPainter(Icons.Filled.Favorite)
                else
                    rememberVectorPainter(Icons.Filled.FavoriteBorder),
                contentDescription = null,
                tint = Color.White
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                color = Color.White,
                text = category
            )
        }
    }
}

@Preview
@Composable
fun MainDrinkCardPreview() {
    MainDrinkCard(
        modifier = Modifier,
        id = "1",
        name = "Mojito lungo aa",
        category = "Cocktail",
        imageString = "https://res.cloudinary.com/dyhi3yudn/image/upload/v1743889914/aviation.jpg",
        isFavorite = true
    )
}