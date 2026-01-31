package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.presentation.main.presentation.MainFilterItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainFilterView(
    modifier: Modifier = Modifier,
    filterList: List<MainFilterItem>
) {
    var expanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        // collapsed view
        AnimatedVisibility(!expanded) {
            LazyRow(
                modifier = Modifier
                    .height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(filterList) {
                    FilterChip(
                        modifier = Modifier
                            .size(width = 20.dp, height = 6.dp)
                            .padding(horizontal = 5.dp),
                        selected = false,
                        onClick = { }
                    ) { /* nothing to be shown right now */ }
                }
            }
        }

        // expanded view
        AnimatedVisibility(expanded) {
            LazyRow(
                modifier = Modifier
                    .height(60.dp)
            ) {
                items(filterList) {
                    FilterChip(
                        modifier = Modifier
                            .padding(horizontal = 5.dp),
                        selected = false,
                        leadingIcon = { Icon(imageVector = it.icon, contentDescription = "") },
                        onClick = { }
                    ) {
                        Text(
                            text = it.filter
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainFilterViewPreview(modifier: Modifier = Modifier) {
    MainFilterView(
        modifier = modifier,
        filterList = MainFilterItem.entries
    )
}