package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.logging.AnacletoLogger
import kotlin.math.absoluteValue

@Composable
fun FadeEdgeContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.drawWithContent {
            drawContent()
            drawRect(
                brush = Brush.horizontalGradient(
                    0.0f to Color.Black.copy(alpha = 0.6f),
                    0.1f to Color.Transparent,
                    0.9f to Color.Transparent,
                    1.0f to Color.Black.copy(alpha = 0.6f)
                ),
                blendMode = BlendMode.Dst
            )
        }
    ) {
        content()
    }
}

enum class CircularCarouselOrientation {
    HORIZONTAL, VERTICAL
}

@Composable
fun <T> CircularCarouselWithAnimations(
    modifier: Modifier = Modifier,
    list: List<T>,
    startIndex: Int = 0,
    orientation: CircularCarouselOrientation = CircularCarouselOrientation.HORIZONTAL,
    padding: Dp = 0.dp,
    currentItem: (T) -> Unit,
    itemContent: @Composable (item: T) -> Unit
) {
    val totalItemsSize = list.size * INFINITE_SCROLL_LIST_SIZE_MULTIPLIER
    val firstIndex = (totalItemsSize / 2) + startIndex
    val pagerState = rememberPagerState(
        initialPage = firstIndex,
        pageCount = { totalItemsSize }
    )

    FadeEdgeContainer {
        val pagerContent: @Composable PagerScope.(Int) -> Unit = { index ->
            val actualIndex = index.mod(list.size)
            val item = list[actualIndex]
            val pageOffset = pagerState
                .calculateCurrentOffsetForPage(index)

            AnacletoLogger.mumbling(
                mumble = "Current item: $item, index: $index, actualIndex: $actualIndex, pageOffset: $pageOffset"
            )

            Box(
                modifier = modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        // leggero effetto 3D/zoom
                        scaleX = 1f - pageOffset.absoluteValue * 0.2f
                        scaleY = 1f - pageOffset.absoluteValue * 0.2f
                        alpha = 1f - 0.5f * pageOffset.absoluteValue
                    },
                contentAlignment = Alignment.Center
            ) {
                itemContent(item)
            }
            if (pageOffset == 0.0f)
                currentItem(item)
        }

        when (orientation) {
            CircularCarouselOrientation.HORIZONTAL ->
                HorizontalPager(
                    modifier = modifier,
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = padding),
                    pageSpacing = 16.dp,
                    pageContent = pagerContent
                )

            CircularCarouselOrientation.VERTICAL ->
                VerticalPager(
                    modifier = modifier,
                    state = pagerState,
                    contentPadding = PaddingValues(vertical = padding),
                    pageSpacing = 16.dp,
                    pageContent = pagerContent
                )
        }
    }
}

private fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@Composable
fun CircularCarouselIngredientItem(
    modifier: Modifier = Modifier,
    ingredient: String,
) {
    Text(
        modifier = modifier,
        text = ingredient,
        color = Color.White,
        fontSize = TextUnit(20f, TextUnitType.Sp)
    )
}

private const val INFINITE_SCROLL_LIST_SIZE_MULTIPLIER = 100

@Preview
@Composable
fun CircularCarouselWithAnimationsPreview(
    @PreviewParameter(CircularCarouselOrientationProvider::class) orientation: CircularCarouselOrientation
) {
    val list = listOf(
        "Tequila" to "1cl",
        "Sugar" to "9cl",
        "Vinegar" to "3cl",
        "Brandy" to "0.5cl",
        "Lemon juice" to "4cl",
        "Rum" to "5cl",
    )

    CircularCarouselWithAnimations(
        modifier = Modifier.fillMaxWidth(),
        list = list,
        startIndex = 2,
        orientation = orientation,
        padding = if (orientation == CircularCarouselOrientation.VERTICAL) 400.dp else 120.dp,
        currentItem = { index ->
            print("Current index: $index")
        }
    ) { item ->
        CircularCarouselIngredientItem(
            ingredient = item.first,
        )
    }
}

class CircularCarouselOrientationProvider : PreviewParameterProvider<CircularCarouselOrientation> {
    override val values: Sequence<CircularCarouselOrientation> =
        sequenceOf(CircularCarouselOrientation.HORIZONTAL, CircularCarouselOrientation.VERTICAL)
}