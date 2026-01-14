package com.zioanacleto.speakeazy.core.domain.main.model

data class MainModel(
    val drinks: List<DrinkModel>
)

data class DrinkModel(
    val id: String,
    val name: String,
    val category: String = "",
    val instructions: String = "",
    val instructionsIt: String = "",
    val glass: String = "",
    val isAlcoholic: Boolean = false,
    var ingredients: List<IngredientModel> = listOf(),
    val imageUrl: String = "",
    val type: String = "",
    val method: String = "",
    val favorite: Boolean = false,
    val visualizations: Long = 0,
    val username: String = "",
    val userId: String = "1"
)

data class IngredientModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val measureCl: String? = null,
    val measureOz: String? = null,
    val measureSpecial: String? = null
)