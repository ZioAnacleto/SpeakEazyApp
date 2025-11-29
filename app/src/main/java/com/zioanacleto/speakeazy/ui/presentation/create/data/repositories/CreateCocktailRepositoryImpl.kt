package com.zioanacleto.speakeazy.ui.presentation.create.data.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.ui.presentation.create.data.datasources.CreateCocktailDataSource
import com.zioanacleto.speakeazy.ui.presentation.create.data.datasources.CreateCocktailUploadDataSource
import com.zioanacleto.speakeazy.ui.presentation.create.domain.CreateCocktailRepository
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CreateCocktailRepositoryImpl(
    private val localDataSource: CreateCocktailDataSource,
    private val networkDataSource: CreateCocktailUploadDataSource,
    private val ingredientDataSource: IngredientDataSource,
    private val dispatcherProvider: DispatcherProvider
) : CreateCocktailRepository {

    override fun getCreateCocktail(): Flow<Resource<List<CreateCocktailModel>>> =
        flow {
            emit(
                localDataSource.getLocalCreateCocktail()
            )
        }.flowOn(dispatcherProvider.io())

    override suspend fun saveCreateCocktail(
        createCocktail: CreateCocktailModel
    ) = localDataSource.saveLocalCreateCocktail(createCocktail)

    override suspend fun deleteCreateCocktail(uniqueId: Int?): Boolean {
        return localDataSource.deleteLocalCreateCocktail(uniqueId)
    }

    override fun getIngredients(): Flow<Resource<IngredientsModel>> =
        flow {
            emit(
                ingredientDataSource.getIngredientsList()
            )
        }.flowOn(dispatcherProvider.io())

    override suspend fun uploadCocktail(createCocktail: CreateCocktailModel): Boolean {
        val response = networkDataSource.uploadCocktail(createCocktail)

        return response is Resource.Success
    }
}