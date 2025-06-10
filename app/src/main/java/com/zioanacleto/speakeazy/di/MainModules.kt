package com.zioanacleto.speakeazy.di

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import com.zioanacleto.speakeazy.domain.models.DataContext
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datamappers.IngredientsDataMapper
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientDataSource
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientLocalDataSource
import com.zioanacleto.speakeazy.ui.presentation.detail.data.datasources.IngredientNetworkDataSource
import com.zioanacleto.speakeazy.ui.presentation.detail.data.dto.IngredientsListDTO
import com.zioanacleto.speakeazy.ui.presentation.detail.data.repositories.DetailRepositoryImpl
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.DetailRepository
import com.zioanacleto.speakeazy.ui.presentation.detail.domain.model.IngredientsModel
import com.zioanacleto.speakeazy.ui.presentation.detail.presentation.DetailViewModel
import com.zioanacleto.speakeazy.ui.presentation.favorites.data.FavoritesRepositoryImpl
import com.zioanacleto.speakeazy.ui.presentation.favorites.data.datamappers.FavoritesDataMapper
import com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources.FavoritesDataSource
import com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources.FavoritesLocalDataSource
import com.zioanacleto.speakeazy.ui.presentation.favorites.data.datasources.FavoritesNetworkDataSource
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.FavoritesRepository
import com.zioanacleto.speakeazy.ui.presentation.favorites.domain.model.FavoritesModel
import com.zioanacleto.speakeazy.ui.presentation.favorites.presentation.FavoritesViewModel
import com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers.HomeDataMapper
import com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers.MainDataMapper
import com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers.MainListDataMapper
import com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers.MainSpeakEazyBEDataMapper
import com.zioanacleto.speakeazy.ui.presentation.main.data.datamappers.MainSpeakEazyBEListDataMapper
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.HomeDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.HomeNetworkDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.MainDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.MainLocalDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.data.datasources.MainNetworkDataSource
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.HomeSectionResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEListResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.repositories.HomeRepositoryImpl
import com.zioanacleto.speakeazy.ui.presentation.main.data.repositories.MainRepositoryImpl
import com.zioanacleto.speakeazy.ui.presentation.main.domain.HomeRepository
import com.zioanacleto.speakeazy.ui.presentation.main.domain.MainRepository
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.MainModel
import com.zioanacleto.speakeazy.ui.presentation.main.presentation.MainViewModel
import com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers.SearchRequestDataMapper
import com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers.SearchResponseDataMapper
import com.zioanacleto.speakeazy.ui.presentation.search.data.datamappers.TagsDataMapper
import com.zioanacleto.speakeazy.ui.presentation.search.data.datasources.NetworkSearchDataSource
import com.zioanacleto.speakeazy.ui.presentation.search.data.datasources.SearchDataSource
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.SearchResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.dto.TagsResponseDTO
import com.zioanacleto.speakeazy.ui.presentation.search.data.repositories.SearchRepositoryImpl
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchRepository
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchModel
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.TagsModel
import com.zioanacleto.speakeazy.ui.presentation.search.presentation.SearchViewModel
import com.zioanacleto.speakeazy.ui.presentation.user.data.datasources.UserDataSource
import com.zioanacleto.speakeazy.ui.presentation.user.data.datasources.UserLocalDataSource
import com.zioanacleto.speakeazy.ui.presentation.user.data.repositories.UserRepositoryImpl
import com.zioanacleto.speakeazy.ui.presentation.user.domain.UserRepository
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

// Singleton
val singletonModule = module {
    single { ApiClientImpl() }
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}

// ViewModels
val viewModelModule = module {
    factory { MainViewModel(get(), get()) }
    factory { DetailViewModel(get(), get(), get()) }
    factory { SearchViewModel(get(), get()) }
    factory { UserViewModel(get(), get()) }
    factory { FavoritesViewModel(get()) }
}

// DataSource
val dataSourceModule = module {
    factory<MainDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        MainNetworkDataSource(
            apiClient = get(),
            dataMapper = get(named("MainSpeakEazyBEDataMapper")),
            listDataMapper = get(named("MainSpeakEazyBEListDataMapper"))
        )
    }
    factory<MainDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        MainLocalDataSource(
            context = androidContext()
        )
    }
    factory<IngredientDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        IngredientNetworkDataSource(
            apiClient = get(),
            dataMapper = get(named("IngredientsDataMapper"))
        )
    }
    factory<IngredientDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        IngredientLocalDataSource(
            context = androidContext()
        )
    }
    factory<HomeDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        HomeNetworkDataSource(
            apiClient = get(),
            dataMapper = get(named("HomeDataMapper"))
        )
    }
    factory<UserDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        UserLocalDataSource(
            context = androidContext()
        )
    }
    factory<FavoritesDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        FavoritesNetworkDataSource(
            apiClient = get(),
            dataMapper = get(named("FavoritesDataMapper"))
        )
    }
    factory<FavoritesDataSource>(
        named(DataContext.LOCAL.name)
    ) {
        FavoritesLocalDataSource(
            context = androidContext()
        )
    }
    factory<SearchDataSource>(
        named(DataContext.NETWORK.name)
    ) {
        NetworkSearchDataSource(
            apiClient = get(),
            requestDataMapper = get(named("SearchRequestDataMapper")),
            responseDataMapper = get(named("SearchResponseDataMapper")),
            tagsDataMapper = get(named("TagsResponseDataMapper")),
            mainDataMapper = get(named("MainSpeakEazyBEListDataMapper"))
        )
    }
}

// DataMappers
val dataMapperModule = module {
    factory<DataMapper<MainResponseDTO, MainModel>>(
        named("MainDataMapper")
    ) { MainDataMapper() }
    factory<DataMapper<MainListResponseDTO, MainModel>>(
        named("MainListDataMapper")
    ) { MainListDataMapper() }
    factory<DataMapper<MainSpeakEazyBEResponseDTO, MainModel>>(
        named("MainSpeakEazyBEDataMapper")
    ) { MainSpeakEazyBEDataMapper() }
    factory<DataMapper<MainSpeakEazyBEListResponseDTO, MainModel>>(
        named("MainSpeakEazyBEListDataMapper")
    ) { MainSpeakEazyBEListDataMapper() }
    factory<DataMapper<IngredientsListDTO, IngredientsModel>>(
        named("IngredientsDataMapper")
    ) { IngredientsDataMapper() }
    factory<DataMapper<HomeSectionResponseDTO, HomeModel>>(
        named("HomeDataMapper")
    ) { HomeDataMapper() }
    factory<DataMapper<MainSpeakEazyBEListResponseDTO, FavoritesModel>>(
        named("FavoritesDataMapper")
    ) { FavoritesDataMapper() }
    factory<DataMapper<String, SearchRequestDTO>>(
        named("SearchRequestDataMapper")
    ) { SearchRequestDataMapper() }
    factory<DataMapper<SearchResponseDTO, SearchModel>>(
        named("SearchResponseDataMapper")
    ) { SearchResponseDataMapper() }
    factory<DataMapper<TagsResponseDTO, TagsModel>>(
        named("TagsResponseDataMapper")
    ) { TagsDataMapper() }
}

// Repositories
val repositoryModule = module {
    factory<MainRepository> {
        MainRepositoryImpl(
            networkDataSource = get(named(DataContext.NETWORK.name)),
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get()
        )
    }
    factory<DetailRepository> {
        DetailRepositoryImpl(
            dataSource = get(named(DataContext.NETWORK.name)),
            dispatcherProvider = get()
        )
    }
    factory<HomeRepository> {
        HomeRepositoryImpl(
            networkDataSource = get(named(DataContext.NETWORK.name)),
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get()
        )
    }
    factory<UserRepository> {
        UserRepositoryImpl(
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get()
        )
    }
    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(
            networkDataSource = get(named(DataContext.NETWORK.name)),
            localDataSource = get(named(DataContext.LOCAL.name)),
            dispatcherProvider = get()
        )
    }
    factory<SearchRepository> {
        SearchRepositoryImpl(
            searchDataSource = get(named(DataContext.NETWORK.name)),
            ingredientDataSource = get(named(DataContext.NETWORK.name)),
            dispatcherProvider = get()
        )
    }
}