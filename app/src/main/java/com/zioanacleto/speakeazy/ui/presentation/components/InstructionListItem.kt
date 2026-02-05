package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.core.domain.main.model.InstructionModel
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun InstructionListItem(
    modifier: Modifier = Modifier,
    instructionModel: InstructionModel,
    isSelected: Boolean,
    isFirstItem: Boolean,
    isLastItem: Boolean,
    onItemClick: (InstructionModel) -> Unit
) {
    val color = if (isSelected) YellowFFE271 else Color.White

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top
    ) {
        StepIndicator(
            isSelected = isSelected,
            showAbove = !isFirstItem,
            showBelow = !isLastItem,
            modifier = Modifier.padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .weight(1f)
                .border(2.dp, color, RoundedCornerShape(8.dp))
                .clickable { onItemClick(instructionModel) }
                .padding(8.dp),
            text = instructionModel.instruction,
            color = Color.White,
            fontSize = TextUnit(18f, TextUnitType.Sp)
        )
    }
}

@Composable
private fun StepIndicator(
    isSelected: Boolean,
    showAbove: Boolean,
    showBelow: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) YellowFFE271 else Color.White

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val dashLine: DrawScope.() -> Unit = {
            drawLine(
                color = Color.White,
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height),
                strokeWidth = size.width,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(8f, 6f)
                )
            )
        }

        // Dashed line above
        Canvas(
            modifier = Modifier
                .width(2.dp)
                .weight(1f)
        ) { if (showAbove) dashLine() }

        // Circle icon
        Box(
            modifier = Modifier
                .size(16.dp)
                .border(2.dp, color, CircleShape)
        )

        // Dashed line below
        Canvas(
            modifier = Modifier
                .width(2.dp)
                .weight(1f)
        ) { if (showBelow) dashLine() }
    }
}

@Preview
@Composable
private fun InstructionListItemPreview(
    @PreviewParameter(BooleanParameterProvider::class) isSelected: Boolean,
) {
    val instruction = InstructionModel(
        type = "testType",
        instruction = "test instruction that would go on two lines, we just need a longer text"
    )
    InstructionListItem(
        modifier = Modifier
            .fillMaxWidth(),
        instructionModel = instruction,
        isSelected = isSelected,
        isFirstItem = isSelected,
        isLastItem = isSelected,
        onItemClick = {}
    )
}

