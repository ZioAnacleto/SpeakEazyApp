package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class DropShape(
    private val cornerRadius: Float = 70f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // val cornerRadius = size.minDimension * CORNER_RADIUS_RATIO
        val path = Path().apply {
            val topLeft = Offset(0f, 0f)
            val firstCorner = Offset(QUARTER_RATIO * size.width, QUARTER_RATIO * size.height)
            val middleCorner = Offset(HALF_RATIO * size.width, HALF_RATIO * size.height)
            val secondCorner = Offset(HALF_RATIO * size.width, THREE_QUARTERS_RATIO * size.height)
            val bottomLeft = Offset(0f, size.height)

            moveTo(topLeft.x, topLeft.y)
            cubicTo(
                x1 = topLeft.x,
                y1 = middleCorner.y - cornerRadius,
                x2 = firstCorner.x + 2 * cornerRadius,
                y2 = topLeft.y,
                x3 = middleCorner.x,
                y3 = middleCorner.y
            )
            cubicTo(
                x1 = middleCorner.x,
                y1 = bottomLeft.y - (0.5f * cornerRadius),
                x2 = secondCorner.x - 2 * cornerRadius,
                y2 = middleCorner.y + cornerRadius,
                x3 = bottomLeft.x,
                y3 = bottomLeft.y
            )
        }

        return Outline.Generic(path)
    }

    companion object {
        private const val HALF_RATIO = 0.5f
        private const val QUARTER_RATIO = 0.25f
        private const val THREE_QUARTERS_RATIO = 0.75f
        private const val CORNER_RADIUS_RATIO = 0.375f
    }
}

@Preview
@Composable
fun DropShapePreview(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .size(200.dp, 200.dp)
            .clip(DropShape())
            .background(Color.White)
    )
}