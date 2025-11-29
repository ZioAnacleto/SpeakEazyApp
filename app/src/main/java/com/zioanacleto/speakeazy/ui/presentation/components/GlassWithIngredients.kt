package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class IngredientLayer(
    val id: String,
    val name: String,
    val measure: String,
    val color: Color,
    var ratio: Float = 1f
)

@Composable
fun GlassWithIngredients(
    modifier: Modifier = Modifier,
    ingredients: List<IngredientLayer>,
    baseInset: Float = 0.05f,
    baseBottomInset: Float = 0.15f,
    animate: Boolean = true,
    animationDuration: Int = 1000,
    onDeleteItemClick: (IngredientLayer) -> Unit = {},
    onItemClick: (IngredientLayer) -> Unit
) {
    val animatedBottomInset by animateFloatAsState(
        targetValue = baseBottomInset,
        animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
        label = "animatedBottomInset"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Bottom,
            userScrollEnabled = false,
            reverseLayout = true
        ) {
            val totalLayers = ingredients.size.toFloat()
            // convert to inset fractions
            val topWidth = 1f - baseInset
            val bottomWidth = 1f - if (animate) animatedBottomInset else baseBottomInset

            // bases ratio
            val trapezoidRatio = topWidth / bottomWidth

            itemsIndexed(
                ingredients,
                key = { _, ingredient -> ingredient.id }
            ) { index, ingredient ->
                // currentIndex: 0 = bottom strip, n-1 = top strip
                val currentIndex = (totalLayers - index - 1)

                // k starts from 1..n
                val k = (currentIndex + 1)

                val topRel = trapezoidRatio + (1f - trapezoidRatio) * (k - 1f) / totalLayers
                val bottomRel = trapezoidRatio + (1f - trapezoidRatio) * k / totalLayers

                // fractions of full width for each strip
                val topWidthFraction = bottomWidth * topRel
                val bottomWidthFraction = bottomWidth * bottomRel

                // actual insets for each strip
                val topInsetForComposable = 1f - topWidthFraction
                val bottomInsetForComposable = 1f - bottomWidthFraction

                // Ogni trapezio anima il cambiamento del suo bordo
                val animatedTopInset by animateFloatAsState(
                    targetValue = topInsetForComposable,
                    animationSpec = tween(durationMillis = animationDuration),
                    label = "animatedTopInset"
                )
                val animatedBottomInsetLocal by animateFloatAsState(
                    targetValue = bottomInsetForComposable,
                    animationSpec = tween(durationMillis = animationDuration),
                    label = "animatedBottomInsetLocal"
                )

                val trapezoidIngredient = ingredient.name to ingredient.measure

                GlassTrapezoid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(ingredient.ratio)
                        .clickable { onItemClick(ingredient) }
                        .animateItem(tween(animationDuration, easing = FastOutSlowInEasing)),
                    topInsetFraction = if (animate) animatedTopInset else topInsetForComposable,
                    bottomInsetFraction = if (animate) animatedBottomInsetLocal else bottomInsetForComposable,
                    bodyColor = ingredient.color,
                    ingredient = trapezoidIngredient,
                    onDeleteItemClick = { itemToBeDeleted ->
                        onDeleteItemClick(
                            ingredients.first { it.name == itemToBeDeleted.first }
                        )
                    }
                )
            }
        }

        GlassOutline(
            modifier = Modifier.matchParentSize(),
            borderColor = Color.White.copy(alpha = 0.8f),
            topInsetFraction = baseInset,
            bottomInsetFraction = baseBottomInset
        )
    }
}

@Preview
@Composable
fun GlassWithIngredientsPreview() {
    val ingredients = remember {
        mutableStateListOf(
            IngredientLayer(
                id = "1",
                name = "Zucchero di canna",
                measure = "3cl",
                color = Color(0xFFB5651D),
                ratio = 0.34f
            ),
            IngredientLayer(
                id = "3",
                name = "Rum",
                measure = "3cl",
                color = Color(0xFFB5651D),
                ratio = 0.33f
            ),
            IngredientLayer(
                id = "98",
                name = "Coca Cola",
                measure = "3cl",
                color = Color(0xFFB5651D),
                ratio = 0.33f
            ),
        )
    }

    Box(
        modifier = Modifier
            .size(width = 300.dp, height = 500.dp)
    ) {
        Button(
            onClick = {
                ingredients.add(
                    IngredientLayer(
                        id = "8",
                        name = "Succo di limone",
                        measure = "3cl",
                        color = Color(0xFFB5651D),
                        ratio = 0.25f
                    )
                )

                ingredients.map {
                    it.ratio = 1f / ingredients.size
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            Text(text = "Aggiungi ingrediente")
        }

        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 450.dp)
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            GlassWithIngredients(ingredients = ingredients) {}
        }
    }
}