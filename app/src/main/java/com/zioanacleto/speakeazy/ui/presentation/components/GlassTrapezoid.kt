package com.zioanacleto.speakeazy.ui.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.R

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun <T : Pair<String, String>> GlassTrapezoid(
    modifier: Modifier = Modifier,
    topInsetFraction: Float = 0.05f,
    bottomInsetFraction: Float = 0.15f,
    bodyColor: Color = Color(0xFFFFE271),
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    backgroundGradient: Brush = Brush.verticalGradient(
        colors = listOf(bodyColor.copy(alpha = 0.9f), bodyColor.copy(alpha = 0.6f))
    ),
    ingredient: T,
    deleteIconOffset: Float = 0.0f, // todo: finish this
    onDeleteItemClick: (T) -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val density = LocalDensity.current
        val widthPx = constraints.maxWidth.toFloat()
        val rawInsetPx = widthPx * bottomInsetFraction * 1.4f

        val offsetX = with(density) { (-rawInsetPx).toDp() }
        val offsetY = 12.dp

        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height
            val leftTop = Offset(x = width * topInsetFraction, y = 0f)
            val rightTop = Offset(x = width * (1f - topInsetFraction), y = 0f)
            val leftBottom = Offset(x = width * bottomInsetFraction, y = height)
            val rightBottom = Offset(x = width * (1f - bottomInsetFraction), y = height)

            val path = Path().apply {
                moveTo(leftTop.x, leftTop.y)
                lineTo(rightTop.x, rightTop.y)
                lineTo(rightBottom.x, rightBottom.y)
                lineTo(leftBottom.x, leftBottom.y)
                close()
            }

            // Fill with gradient (scaled to canvas size)
            drawPath(
                path = path,
                brush = backgroundGradient,
                style = Fill
            )

            // Optional inner highlight: a slightly narrower trapezoid to give depth
            val insetPx = width * 0.04f
            val innerPath = Path().apply {
                moveTo(leftTop.x + insetPx, leftTop.y + insetPx)
                lineTo(rightTop.x - insetPx, rightTop.y + insetPx)
                lineTo(rightBottom.x - insetPx, rightBottom.y - insetPx)
                lineTo(leftBottom.x + insetPx, leftBottom.y - insetPx)
                close()
            }

            drawPath(
                path = innerPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent)
                ),
                style = Fill
            )

            // Border stroke
            drawPath(
                path = path,
                color = borderColor,
                style = Stroke(
                    width = borderWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = ingredient.first,
                    color = Color.Black,
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = ingredient.second,
                    color = Color.Black,
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                )
            }

            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = offsetX, y = -offsetY)
                    .align(Alignment.BottomEnd)
                    .clickable { onDeleteItemClick(ingredient) },
                painter = painterResource(R.drawable.create_cocktail_trash_icon),
                contentDescription = "Delete icon",
                tint = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun GlassTrapezoidPreview() {
    GlassTrapezoid(
        modifier = Modifier
            .size(160.dp, 220.dp)
            .padding(4.dp),
        topInsetFraction = 0.05f,
        bottomInsetFraction = 0.15f,
        bodyColor = Color(0xFFBEE3F8), // colore del liquido / vetro
        borderColor = Color(0xFF333333),
        borderWidth = 2.dp,
        ingredient = "TestIngredient lungo" to "4cl"
    )
}