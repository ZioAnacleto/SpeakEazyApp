package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.tan

class TrapezoidShape(
    private val cornerRadius: Float = 30f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val topLeft = Offset(0f, 0f)
            val topRight = Offset(size.width, size.height * PATH_FIRST_LINE_WIDTH_RATIO)
            val bottomRight = Offset(size.width, size.height * PATH_SECOND_LINE_WIDTH_RATIO)
            val bottomLeft = Offset(0f, size.height)

            moveTo(topLeft.x, topLeft.y)
            lineTo(topRight.x - cornerRadius, topRight.y)
            quadraticTo(topRight.x, topRight.y, topRight.x, topRight.y + cornerRadius)
            lineTo(bottomRight.x, bottomRight.y - cornerRadius)
            quadraticTo(bottomRight.x, bottomRight.y, bottomRight.x - cornerRadius, bottomRight.y)
            lineTo(bottomLeft.x, bottomLeft.y)
            close()
        }

        return Outline.Generic(path)
    }

    companion object {
        private const val PATH_FIRST_LINE_WIDTH_RATIO = 0.22f
        private const val PATH_SECOND_LINE_WIDTH_RATIO = 0.78f
    }
}