package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem

@Composable
fun ExpandableHorizontalFilterView(
    modifier: Modifier = Modifier,
    list: List<SearchFilterItem>,
    onFilterSelectClick: (SearchFilterItem) -> List<SelectedFilter> = { emptyList() },
    onFilterDoneClick: (List<SelectedFilter>) -> Unit = { }
) {
    val filterItemHeight = 50.dp
    // todo: change them
    val gradients = listOf(
        Brush.horizontalGradient(
            colors = listOf(
                Color(52, 10, 72, 255),
                Color(70, 20, 93, 255),
                Color(85, 29, 111, 255)
            )
        ),
        Brush.horizontalGradient(
            colors = listOf(
                Color(53, 27, 66, 255),
                Color(77, 30, 99, 255),
                Color(101, 26, 136, 255)
            )
        ),
        Brush.horizontalGradient(
            colors = listOf(
                Color(52, 10, 72, 255),
                Color(70, 20, 93, 255),
                Color(85, 29, 111, 255)
            )
        ),
        Brush.horizontalGradient(
            colors = listOf(
                Color(53, 27, 66, 255),
                Color(77, 30, 99, 255),
                Color(101, 26, 136, 255)
            )
        )
    )

    Box(
        modifier = modifier
    ) {
        var visible by remember { mutableStateOf(false) }
        var selectedFilter by remember { mutableStateOf<SearchFilterItem?>(null) }
        var filterList by remember { mutableStateOf(listOf<SelectedFilter>()) }

        AnimatedVisibility(
            visible = visible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(160.dp, 500.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .speakEazyInvertedGradientBackground()
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(80.dp),
                    modifier = Modifier
                        .padding(
                            top = filterItemHeight + 10.dp,
                            start = 10.dp,
                            bottom = 10.dp,
                            end = 10.dp
                        ),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filterList) { (name, isSelected) ->
                        var isItemSelected by remember { mutableStateOf(isSelected) }
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isItemSelected) Color.White else Color.DarkGray
                                )
                                .padding(10.dp)
                                .clickable {
                                    isItemSelected = !isItemSelected
                                    filterList = filterList.map {
                                        if (it.first == name)
                                            it.copy(second = isItemSelected)
                                        else
                                            it
                                    }
                                },
                            text = name,
                            color = if (isItemSelected) Color.Black else Color.White
                        )
                    }
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            itemsIndexed(list) { index, filter ->
                val isFilterSelected = filter == selectedFilter

                Row(
                    modifier = Modifier
                        .height(filterItemHeight)
                        .fillParentMaxWidth(fraction = (1f / list.size))
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                        .conditionalBackground(
                            useColor = isFilterSelected,
                            color = filter.selectedColor,
                            gradient = gradients[index]
                        )
                        .zIndex(if (!isFilterSelected) 0f else 10f)
                        .clickable {
                            if (!visible) {
                                filterList = onFilterSelectClick(filter)
                                selectedFilter = filter
                            } else {
                                onFilterDoneClick(filterList)
                                if (!filterList.any { it.second })
                                    selectedFilter = null
                            }
                            visible = !visible
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 12.dp),
                        painter = painterResource(filter.icon),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp, end = 12.dp),
                        color = if (!isFilterSelected) Color.LightGray else Color.Black,
                        text = filter.label,
                        fontSize = TextUnit(11f, TextUnitType.Sp),
                        textAlign = TextAlign.Center,
                        lineHeight = TextUnit(14f, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

private fun Modifier.conditionalBackground(
    useColor: Boolean,
    color: Color,
    gradient: Brush
) = if(useColor) this.background(color) else this.background(gradient)

@Preview
@Composable
fun ExpandableHorizontalFilterViewPreview() {
    ExpandableHorizontalFilterView(
        list = SearchFilterItem.entries.map { it }
    )
}

typealias SelectedFilter = Pair<String, Boolean>