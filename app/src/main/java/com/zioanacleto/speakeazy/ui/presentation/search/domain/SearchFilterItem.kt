package com.zioanacleto.speakeazy.ui.presentation.search.domain

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

enum class SearchFilterItem(
    val label: String,
    @DrawableRes val icon: Int,
    val selectedColor: Color
) {
    INGREDIENTS(
        label = "Item",
        icon = R.drawable.ingredients_filter_icon,
        selectedColor = YellowFFE271
    ),
    TAGS(
        label = "Tags",
        icon = R.drawable.tags_filter_icon,
        selectedColor = YellowFFE271
    ),
    METHOD(
        label = "Method",
        icon = R.drawable.cocktail_filter_icon,
        selectedColor = YellowFFE271
    ),
    TYPE(
        label = "Type",
        icon = R.drawable.ingredients_filter_icon,
        selectedColor = YellowFFE271
    )
}