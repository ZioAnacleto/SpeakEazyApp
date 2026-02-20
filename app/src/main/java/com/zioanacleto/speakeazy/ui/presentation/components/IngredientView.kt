package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zioanacleto.speakeazy.core.domain.main.model.IngredientModel
import com.zioanacleto.speakeazy.ui.theme.BottomBarBackground

// todo: detail?
@Composable
fun IngredientView(ingredient: IngredientModel) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .size(width = 80.dp, height = 120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(BottomBarBackground),
            contentDescription = "Ingredient image",
            model = ingredient.imageUrl,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .height(120.dp)
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                text = ingredient.name,
                color = Color.White,
                fontSize = TextUnit(20f, TextUnitType.Sp)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = "${ingredient.measureCl ?: ingredient.measureSpecial}",
                color = Color.White
            )
        }
    }
}