package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassOutline(
    modifier: Modifier = Modifier,
    topInsetFraction: Float = 0.05f,
    bottomInsetFraction: Float = 0.15f,
    borderColor: Color = Color(0xFFAAAAAA),
    borderWidth: Dp = 2.dp
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            val leftTop = Offset(w * topInsetFraction, 0f)
            val rightTop = Offset(w * (1f - topInsetFraction), 0f)
            val leftBottom = Offset(w * bottomInsetFraction, h)
            val rightBottom = Offset(w * (1f - bottomInsetFraction), h)

            val path = Path().apply {
                moveTo(leftTop.x, leftTop.y)
                lineTo(rightTop.x, rightTop.y)
                lineTo(rightBottom.x, rightBottom.y)
                lineTo(leftBottom.x, leftBottom.y)
                close()
            }

            // Glass' border
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
    }
}

@Preview
@Composable
fun GlassOutlinePreview() {
    GlassOutline(
        modifier = Modifier
            .size(120.dp, 180.dp)
            .padding(vertical = 4.dp),
        topInsetFraction = 0.08f,
        bottomInsetFraction = 0.18f,
        borderColor = Color.White.copy(alpha = 0.8f),
        borderWidth = 2.dp
    )
}