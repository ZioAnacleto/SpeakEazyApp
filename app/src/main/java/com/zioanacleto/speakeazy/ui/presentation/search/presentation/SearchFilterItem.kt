package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.core.domain.search.model.SearchFilterModel
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

sealed class SearchFilterItem(
    val id: SearchFilterModel,
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int,
    val selectedColor: Color
) {
    data object Ingredients : SearchFilterItem(
        id = SearchFilterModel.INGREDIENTS,
        label = R.string.search_filter_item__ingredients_label,
        icon = R.drawable.ingredients_filter_icon,
        selectedColor = YellowFFE271
    )

    data object Tags : SearchFilterItem(
        id = SearchFilterModel.TAGS,
        label = R.string.search_filter_item__tags_label,
        icon = R.drawable.tags_filter_icon,
        selectedColor = YellowFFE271
    )

    data object Method : SearchFilterItem(
        id = SearchFilterModel.METHOD,
        label = R.string.search_filter_item__method_label,
        icon = R.drawable.cocktail_filter_icon,
        selectedColor = YellowFFE271
    )

    data object Type : SearchFilterItem(
        id = SearchFilterModel.TYPE,
        label = R.string.search_filter_item__type_label,
        icon = R.drawable.ingredients_filter_icon,
        selectedColor = YellowFFE271
    )
}