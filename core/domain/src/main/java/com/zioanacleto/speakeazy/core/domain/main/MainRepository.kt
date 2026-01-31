package com.zioanacleto.speakeazy.core.domain.main

import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getMainList(): Flow<Resource<MainModel>>
    fun getMainById(id: String): Flow<Resource<MainModel>>
    suspend fun setFavoriteCocktail(cocktailId: String, cocktailName: String)
    suspend fun deleteFavoriteCocktail(cocktailId: String)
    suspend fun updateVisualizations(cocktailId: String)
}