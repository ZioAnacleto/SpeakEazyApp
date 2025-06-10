package com.zioanacleto.speakeazy.ui.presentation.main.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material.icons.sharp.Face
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material.icons.sharp.ThumbUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

enum class MainFilterItem(
    val filter: String,
    val field: String,
    val icon: ImageVector,
    val selectedColor: Color
) {
    UNFORGETTABLES(
        filter = "The Unforgettables",
        field = "category",
        icon = Icons.Sharp.Star,
        selectedColor = YellowFFE271
    ),
    LONG_DRINK(
        filter = "Long Drink",
        field = "type",
        icon = Icons.Sharp.ThumbUp,
        selectedColor = YellowFFE271
    ),
    VIRGIN(
        filter = "Virgin",
        field = "isAlcoholic",
        icon = Icons.Sharp.Clear,
        selectedColor = YellowFFE271
    ),
    BUILD(
        filter = "Build",
        field = "method",
        icon = Icons.Sharp.Build,
        selectedColor = YellowFFE271
    ),
    SHAKE_AND_STRAIN(
        filter = "Shake & Strain",
        field = "method",
        icon = Icons.Sharp.Face,
        selectedColor = YellowFFE271
    )
}