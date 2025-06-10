package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem

@Composable
fun ExpandableVerticalFilterView(
    modifier: Modifier = Modifier,
    list: List<SearchFilterItem>,
    onFilterSelectClick: (SearchFilterItem) -> List<SelectedFilter> = { emptyList() },
    onFilterDoneClick: (List<SelectedFilter>) -> Unit = { }
) {
    var visible by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf<SearchFilterItem?>(null) }
    var filterList by remember { mutableStateOf(listOf<SelectedFilter>()) }

    Box(
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Column(
                modifier = Modifier
                    .size(width = 360.dp, height = 380.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(20.dp),
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(20.dp)
                        )
                    )
                    .speakEazyInvertedGradientBackground()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 140.dp, top = 10.dp)
                ) {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 14.dp),
                            text = selectedFilter?.label.default(),
                            fontSize = TextUnit(20f, TextUnitType.Sp)
                        )
                    }
                    items(filterList) { (name, isChecked) ->
                        var isItemChecked by remember { mutableStateOf(isChecked) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isItemChecked,
                                onCheckedChange = {
                                    isItemChecked = !isItemChecked
                                    filterList = filterList.map {
                                        if (it.first == name)
                                            it.copy(second = isItemChecked)
                                        else
                                            it
                                    }
                                }
                            )

                            Text(
                                color = Color.LightGray,
                                text = name,
                                fontSize = TextUnit(14f, TextUnitType.Sp)
                            )
                        }
                    }
                }
            }
        }

        LazyColumn {
            itemsIndexed(list) { index, filter ->
                val filterTranslationY = ((-20) * index).dp
                val isFilterSelected = filter == selectedFilter

                Column(
                    modifier = Modifier
                        .size(width = 62.dp, height = 140.dp)
                        .graphicsLayer {
                            if (index != 0)
                                translationY = filterTranslationY.toPx()
                        }
                        .clip(TrapezoidShape())
                        .background(
                            if (!isFilterSelected) Color.DarkGray else filter.selectedColor
                        )
                        .zIndex(if (!isFilterSelected) 0f else 10f)
                        .clickable {
                            if (!visible) {
                                filterList = onFilterSelectClick(filter)
                                selectedFilter = filter
                            } else {
                                onFilterDoneClick(filterList)
                                if(!filterList.any { it.second })
                                    selectedFilter = null
                            }
                            visible = !visible
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(34.dp),
                        painter = painterResource(filter.icon),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 12.dp, start = 5.dp, end = 5.dp),
                        color = if (!isFilterSelected) Color.LightGray else Color.Black,
                        text = filter.label,
                        fontSize = TextUnit(12f, TextUnitType.Sp),
                        textAlign = TextAlign.Center,
                        lineHeight = TextUnit(14f, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ExpandableVerticalFilterViewPreview() {
    ExpandableVerticalFilterView(
        list = SearchFilterItem.entries.map { it }
    )
}

typealias SelectedFilter = Pair<String, Boolean>