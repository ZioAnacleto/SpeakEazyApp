package com.zioanacleto.speakeazy.core.data.main.datasources

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel

interface MainDataSource {
    suspend fun getMainList(): Resource<MainModel>
    suspend fun getMainById(id: String): Resource<MainModel>
    suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String)
    suspend fun deleteFavoriteCocktail(cocktailId: String)
    suspend fun updateVisualizations(cocktailId: String)
}