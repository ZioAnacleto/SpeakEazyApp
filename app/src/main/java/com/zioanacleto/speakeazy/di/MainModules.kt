package com.zioanacleto.speakeazy.di

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.speakeazy.MainActivityViewModel
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.Cocktail3DModelDataMapper
import com.zioanacleto.speakeazy.core.data.cocktail3d.datamapper.CocktailSceneDataMapper
import com.zioanacleto.speakeazy.core.data.user.provider.FirebaseActionCodeSettingsProvider
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller.Cocktail3DSceneController
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller.Cocktail3DSceneControllerImpl
import com.zioanacleto.speakeazy.ui.presentation.create.presentation.CreateCocktailViewModel
import com.zioanacleto.speakeazy.ui.presentation.detail.presentation.DetailViewModel
import com.zioanacleto.speakeazy.ui.presentation.favorites.presentation.FavoritesViewModel
import com.zioanacleto.speakeazy.ui.presentation.main.presentation.MainViewModel
import com.zioanacleto.speakeazy.ui.presentation.search.presentation.SearchViewModel
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserViewModel
import com.zioanacleto.speakeazy.ui.presentation.user.provider.FirebaseActionCodeSettingsProviderImpl
import org.koin.dsl.module

// Singleton
val singletonModule = module {
    single { ApiClientImpl() }
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<Cocktail3DSceneController> {
        Cocktail3DSceneControllerImpl(
            get(),
            get(getNamedClass<Cocktail3DModelDataMapper>()),
            get(getNamedClass<CocktailSceneDataMapper>())
        )
    }
    factory<FirebaseActionCodeSettingsProvider> { FirebaseActionCodeSettingsProviderImpl() }
}

// ViewModels
val viewModelModule = module {
    factory { MainViewModel(get(), get()) }
    factory { DetailViewModel(get(), get(), get()) }
    factory { SearchViewModel(get(), get()) }
    factory { UserViewModel(get<UserRepository>(), get(), get()) }
    factory { FavoritesViewModel(get(), get()) }
    factory { MainActivityViewModel(get<UserRepository>(), get()) }
    factory { CreateCocktailViewModel(get(), get(), get()) }
}