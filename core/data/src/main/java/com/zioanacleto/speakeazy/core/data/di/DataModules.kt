package com.zioanacleto.speakeazy.core.data.di

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.Cocktail3DModelDataMapper
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.CocktailInfo
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.CocktailSceneDataMapper
import com.zioanacleto.speakeazy.core.data.create.datamappers.CreateCocktailDataMapper
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailDataSource
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailLocalDataSource
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailNetworkUploadDataSource
import com.zioanacleto.speakeazy.core.data.create.datasources.CreateCocktailUploadDataSource
import com.zioanacleto.speakeazy.core.data.create.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.core.data.create.repositories.CreateCocktailRepositoryImpl
import com.zioanacleto.speakeazy.core.data.detail.datamappers.IngredientsDataMapper
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientLocalDataSource
import com.zioanacleto.speakeazy.core.data.detail.datasources.IngredientNetworkDataSource
import com.zioanacleto.speakeazy.core.data.detail.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.core.data.detail.repositories.DetailRepositoryImpl
import com.zioanacleto.speakeazy.core.data.di.models.DataContext
import com.zioanacleto.speakeazy.core.data.favorites.FavoritesRepositoryImpl
import com.zioanacleto.speakeazy.core.data.favorites.datamappers.FavoritesDataMapper
import com.zioanacleto.speakeazy.core.data.favorites.datasources.FavoritesDataSource
import com.zioanacleto.speakeazy.core.data.favorites.datasources.FavoritesLocalDataSource
import com.zioanacleto.speakeazy.core.data.favorites.datasources.FavoritesNetworkDataSource
import com.zioanacleto.speakeazy.core.data.main.datamappers.HomeDataMapper
import com.zioanacleto.speakeazy.core.data.main.datamappers.MainDataMapper
import com.zioanacleto.speakeazy.core.data.main.datamappers.MainListDataMapper
import com.zioanacleto.speakeazy.core.data.main.datamappers.MainSpeakEazyBEDataMapper
import com.zioanacleto.speakeazy.core.data.main.datamappers.MainSpeakEazyBEListDataMapper
import com.zioanacleto.speakeazy.core.data.main.datasources.HomeDataSource
import com.zioanacleto.speakeazy.core.data.main.datasources.HomeNetworkDataSource
import com.zioanacleto.speakeazy.core.data.main.datasources.MainDataSource
import com.zioanacleto.speakeazy.core.data.main.datasources.MainLocalDataSource
import com.zioanacleto.speakeazy.core.data.main.datasources.MainNetworkDataSource
import com.zioanacleto.speakeazy.core.data.main.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainListResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.core.data.main.repositories.HomeRepositoryImpl
import com.zioanacleto.speakeazy.core.data.main.repositories.MainRepositoryImpl
import com.zioanacleto.speakeazy.core.data.search.datamappers.SearchRequestDataMapper
import com.zioanacleto.speakeazy.core.data.search.datamappers.SearchResponseDataMapper
import com.zioanacleto.speakeazy.core.data.search.datamappers.TagsDataMapper
import com.zioanacleto.speakeazy.core.data.search.datasources.LocalSearchQueriesDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.NetworkSearchDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchDataSource
import com.zioanacleto.speakeazy.core.data.search.datasources.SearchQueriesDataSource
import com.zioanacleto.speakeazy.core.data.search.dto.SearchRequestDTO
import com.zioanacleto.speakeazy.core.data.search.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.core.data.search.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.core.data.search.repositories.SearchRepositoryImpl
import com.zioanacleto.speakeazy.core.data.user.datasources.UserDataSource
import com.zioanacleto.speakeazy.core.data.user.datasources.UserLocalDataSource
import com.zioanacleto.speakeazy.core.data.user.repositories.FirebaseAuthRepositoryImpl
import com.zioanacleto.speakeazy.core.data.user.repositories.UserRepositoryImpl
import com.zioanacleto.speakeazy.core.database.dao.CreateCocktailDao
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.core.database.dao.IngredientDao
import com.zioanacleto.speakeazy.core.database.dao.SearchDao
import com.zioanacleto.speakeazy.core.database.dao.UserDao
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.Cocktail3DModel
import com.zioanacleto.speakeazy.core.domain.cocktail3d.model.CocktailScene
import com.zioanacleto.speakeazy.core.domain.create.CreateCocktailRepository
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.domain.detail.DetailRepository
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import com.zioanacleto.speakeazy.core.domain.favorites.FavoritesRepository
import com.zioanacleto.speakeazy.core.domain.favorites.model.FavoritesModel
import com.zioanacleto.speakeazy.core.domain.main.HomeRepository
import com.zioanacleto.speakeazy.core.domain.main.MainRepository
import com.zioanacleto.speakeazy.core.domain.main.model.HomeModel
import com.zioanacleto.speakeazy.core.domain.main.model.MainModel
import com.zioanacleto.speakeazy.core.domain.search.SearchRepository
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.core.domain.search.model.TagsModel
import com.zioanacleto.speakeazy.core.domain.user.FirebaseAuthRepository
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

// DataMappers
val dataMapperModule = module {
    factoryNamedClass<DataMapper<MainResponseDTO, MainModel>, MainDataMapper>()
    factoryNamedClass<DataMapper<MainListResponseDTO, MainModel>, MainListDataMapper>()
    factoryNamedClass<DataMapper<MainSpeakEazyBEResponseDTO, MainModel>, MainSpeakEazyBEDataMapper>()
    factoryNamedClass<DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>, MainSpeakEazyBEListDataMapper>()
    factoryNamedClass<DataMapper<IngredientsListDTO, IngredientsModel>, IngredientsDataMapper>()
    factoryNamedClass<DataMapper<HomeSectionResponseDTO, HomeModel>, HomeDataMapper>()
    factoryNamedClass<DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel>, FavoritesDataMapper>()
    factoryNamedClass<DataMapper<String, SearchRequestDTO>, SearchRequestDataMapper>()
    factoryNamedClass<DataMapper<SearchResponseDTO, SearchModel>, SearchResponseDataMapper>()
    factoryNamedClass<DataMapper<TagsResponseDTO, TagsModel>, TagsDataMapper>()
    factoryNamedClass<DataMapper<CreateCocktailModel, CreateCocktailRequestDTO>, CreateCocktailDataMapper>()
    factoryNamedClass<DataMapper<String, CocktailScene>, CocktailSceneDataMapper>()
    factoryNamedClass<DataMapper<CocktailInfo, Cocktail3DModel>, Cocktail3DModelDataMapper>()
}

// DataSource
val dataSourceModule = module {
    factory<MainDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        MainNetworkDataSource(
            apiClient = get<ApiClientImpl>(),
            dataMapper = get(getNamedClass<MainSpeakEazyBEDataMapper>()),
            listDataMapper = get(getNamedClass<MainSpeakEazyBEListDataMapper>()),
            performanceTracesManager = get()
        )
    }
    factory<MainDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        MainLocalDataSource(
            favoritesDao = get<FavoritesDao>(),
            performanceTracesManager = get()
        )
    }
    factory<IngredientDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        IngredientNetworkDataSource(
            apiClient = get<ApiClientImpl>(),
            dataMapper = get(getNamedClass<IngredientsDataMapper>()),
            performanceTracesManager = get()
        )
    }
    factory<IngredientDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        IngredientLocalDataSource(
            ingredientDao = get<IngredientDao>(),
            performanceTracesManager = get()
        )
    }
    factory<HomeDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        HomeNetworkDataSource(
            apiClient = get<ApiClientImpl>(),
            dataMapper = get(getNamedClass<HomeDataMapper>()),
            performanceTracesManager = get()
        )
    }
    factory<UserDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        UserLocalDataSource(
            userDao = get<UserDao>(),
            performanceTracesManager = get()
        )
    }
    factory<FavoritesDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        FavoritesNetworkDataSource(
            apiClient = get<ApiClientImpl>(),
            dataMapper = get(getNamedClass<FavoritesDataMapper>()),
            performanceTracesManager = get()
        )
    }
    factory<FavoritesDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        FavoritesLocalDataSource(
            favoritesDao = get<FavoritesDao>(),
            performanceTracesManager = get()
        )
    }
    factory<SearchDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        NetworkSearchDataSource(
            apiClient = get<ApiClientImpl>(),
            requestDataMapper = get(getNamedClass<SearchRequestDataMapper>()),
            responseDataMapper = get(getNamedClass<SearchResponseDataMapper>()),
            tagsDataMapper = get(getNamedClass<TagsDataMapper>()),
            mainDataMapper = get(getNamedClass<MainSpeakEazyBEListDataMapper>()),
            performanceTracesManager = get()
        )
    }
    factory<CreateCocktailDataSource> {
        CreateCocktailLocalDataSource(
            createCocktailDao = get<CreateCocktailDao>(),
            performanceTracesManager = get()
        )
    }
    factory<CreateCocktailUploadDataSource> {
        CreateCocktailNetworkUploadDataSource(
            apiClient = get<ApiClientImpl>(),
            requestDataMapper = get(getNamedClass<CreateCocktailDataMapper>()),
            performanceTracesManager = get()
        )
    }
    factory<SearchQueriesDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        LocalSearchQueriesDataSource(
            searchDao = get<SearchDao>(),
            performanceTracesManager = get()
        )
    }
}

// Repositories
val repositoryModule = module {
    factory<MainRepository> {
        MainRepositoryImpl(
            networkDataSource = get(named(DataContext.NETWORK.name)),
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<DetailRepository> {
        DetailRepositoryImpl(
            dataSource = get(named(DataContext.NETWORK.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<HomeRepository> {
        HomeRepositoryImpl(
            networkDataSource = get(named(DataContext.NETWORK.name)),
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<UserRepository> {
        UserRepositoryImpl(
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(
            networkDataSource = get(named(DataContext.NETWORK.name)),
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<SearchRepository> {
        SearchRepositoryImpl(
            searchDataSource = get(named(DataContext.NETWORK.name)),
            ingredientDataSource = get(named(DataContext.NETWORK.name)),
            searchQueriesDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<CreateCocktailRepository> {
        CreateCocktailRepositoryImpl(
            localDataSource = get(),
            networkDataSource = get(),
            ingredientDataSource = get<IngredientDataSource>(named(DataContext.NETWORK.name)),
            dispatcherProvider = get(),
            performanceTracesManager = get()
        )
    }
    factory<FirebaseAuthRepository> {
        FirebaseAuthRepositoryImpl(get())
    }
}