package com.zioanacleto.speakeazy.ui.presentation.main.data.datasources

import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel

interface MainDataSource {
    suspend fun getMainList(): Resource<MainModel>
    suspend fun getMainById(id: String): Resource<MainModel>
    suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String)
    suspend fun deleteFavoriteCocktail(cocktailId: String)
    suspend fun updateVisualizations(cocktailId: String)
}