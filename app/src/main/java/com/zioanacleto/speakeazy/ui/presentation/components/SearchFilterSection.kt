package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import kotlinx.coroutines.delay

@Composable
fun SearchFilterSection(
    modifier: Modifier = Modifier,
    list: List<SearchFilterItem>,
    onFilterSelectClick: (SearchFilterItem) -> List<SelectedFilter> = { emptyList() },
    onFilterDoneClick: (Map<SearchFilterItem, List<SelectedFilter>>) -> Unit = { }
) {
    // view variables
    val collapsedHeight = 40f
    val expandedHeight = 238f
    var currentHeight by remember { mutableFloatStateOf(collapsedHeight) }

    // animation variables
    val expansionProgress =
        ((currentHeight - collapsedHeight) / (expandedHeight - collapsedHeight))
            .coerceIn(0f, 1f)
    val expandedAlpha = ((expansionProgress - 0.2f) / 0.8f).coerceIn(0f, 1f)
    val collapsedAlpha = 1f - expansionProgress

    // this will force the section to collapse
    var collapseRequested by remember { mutableStateOf(false) }

    // logic variables
    var filterState by remember {
        mutableStateOf<Map<SearchFilterItem, List<SelectedFilter>>>(emptyMap())
    }
    val selectedFilterNumber = filterState.values.flatten().count { it.second }
    val buttonEnabled = filterState.values.flatten().any { it.second }

    // we first create filterState using provided lambda
    LaunchedEffect(list) {
        filterState = list.associateWith { filter -> onFilterSelectClick(filter) }
    }

    LaunchedEffect(collapseRequested) {
        if (collapseRequested) {
            val step = 6f
            while (currentHeight > collapsedHeight) {
                currentHeight = (currentHeight - step)
                    .coerceAtLeast(collapsedHeight)
                delay(10) // ~100fps
            }
            collapseRequested = false
        }
    }

    Box(
        modifier = modifier
            .height(currentHeight.dp)
            .draggable(
                enabled = !collapseRequested,
                state = rememberDraggableState { delta ->
                    val newHeight = (currentHeight + delta)
                        .coerceIn(collapsedHeight, expandedHeight)
                    currentHeight = newHeight
                },
                orientation = Orientation.Vertical
            )
            .border(
                width = 1.dp,
                color = if (selectedFilterNumber == 0)
                    Color.DarkGray
                else
                    YellowFFE271,
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
            )
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .speakEazyGradientBackground(),
        contentAlignment = Alignment.CenterStart
    ) {
        // expanded section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 10.dp, bottom = 10.dp)
                .alpha(expandedAlpha),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                list.forEach { filter ->
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        text = filter.label,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnit(16f, TextUnitType.Sp)
                    )

                    val filtersForItem = filterState[filter].orEmpty()
                    LazyRow(
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(
                            items = filtersForItem,
                            key = { it }) { (name, isSelected) ->
                            var isItemSelected by remember { mutableStateOf(isSelected) }

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isItemSelected) Color.White else Color.DarkGray
                                    )
                                    .padding(10.dp)
                                    .clickable {
                                        isItemSelected = !isItemSelected
                                        filterState = filterState.toMutableMap().apply {
                                            this[filter] = filtersForItem.map {
                                                if (it.first == name)
                                                    it.copy(second = !it.second)
                                                else it
                                            }
                                        }
                                    },
                                text = name,
                                color = if (isItemSelected) Color.Black else Color.White,
                                fontSize = TextUnit(14f, TextUnitType.Sp)
                            )
                        }
                    }
                }
            }

            SafeClickableGenericButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(top = 6.dp),
                enabled = buttonEnabled,
                border = BorderStroke(
                    width = 2.dp,
                    color = if (buttonEnabled) YellowFFE271 else Color.DarkGray
                ),
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = YellowFFE271,
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = Color.Transparent
                ),
                onClick = {
                    val allSelectedFilters =
                        filterState.mapValues { (_, values) ->
                            values.filter { it.second }
                        }.filterValues { it.isNotEmpty() }
                    onFilterDoneClick(allSelectedFilters)
                    collapseRequested = true
                }
            ) {
                Text(
                    fontSize = TextUnit(15f, TextUnitType.Sp),
                    text = "Apply filters"
                )
            }
        }

        // collapsed section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(collapsedAlpha),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                color = if (selectedFilterNumber > 0) YellowFFE271 else Color.White,
                text = selectedFilterNumber.filterSectionText(),
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )
        }
    }
}

private fun Int.filterSectionText() = when (this) {
    0 -> "Filter section"
    1 -> "$this filter selected"
    else -> "$this filters selected"
}

@Preview(showBackground = true)
@Composable
fun SearchFilterSectionPreview() {
    SearchFilterSection(
        modifier = Modifier
            .fillMaxWidth(),
        list = listOf(SearchFilterItem.INGREDIENTS, SearchFilterItem.TAGS),
        onFilterSelectClick = {
            when (it) {
                SearchFilterItem.INGREDIENTS -> listOf(
                    "Vodka" to false,
                    "Rum" to false,
                    "Gin" to false
                )

                else -> listOf(
                    "Alcoholic" to true
                )
            }
        }
    )
}
