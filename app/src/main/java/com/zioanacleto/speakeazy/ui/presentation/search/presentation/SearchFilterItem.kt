package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

sealed class SearchFilterItem(
    val id: SearchFilterModel,
    val label: String,
    @DrawableRes val icon: Int,
    val selectedColor: Color
) {
    data object Ingredients : SearchFilterItem(
        id = SearchFilterModel.INGREDIENTS,
        label = "Ingredients",
        icon = R.drawable.ingredients_filter_icon,
        selectedColor = YellowFFE271
    )
    data object Tags : SearchFilterItem(
        id = SearchFilterModel.TAGS,
        label = "Tags",
        icon = R.drawable.tags_filter_icon,
        selectedColor = YellowFFE271
    )
    data object Method : SearchFilterItem(
        id = SearchFilterModel.METHOD,
        label = "Method",
        icon = R.drawable.cocktail_filter_icon,
        selectedColor = YellowFFE271
    )
    data object Type : SearchFilterItem(
        id = SearchFilterModel.TYPE,
        label = "Type",
        icon = R.drawable.ingredients_filter_icon,
        selectedColor = YellowFFE271
    )
}