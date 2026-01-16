package com.zioanacleto.speakeazy.core.data.create.repositories

import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailDataSource
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailUploadDataSource
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.domain.create.CreateCocktailRepository
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
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