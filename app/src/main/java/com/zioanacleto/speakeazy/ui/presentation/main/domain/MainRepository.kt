package com.zioanacleto.speakeazy.ui.presentation.main.domain

import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import kotlinx.coroutines.flow.Flow
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel

interface MainRepository {
    fun getMainList(): Flow<Resource<MainModel>>
    fun getMainById(id: String): Flow<Resource<MainModel>>
    suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String)
    suspend fun deleteFavoriteCocktail(cocktailId: String)
    suspend fun updateVisualizations(cocktailId: String)
}