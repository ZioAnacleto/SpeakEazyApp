package com.zioanacleto.speakeazy.core.database.di

import androidx.room.Room
import com.zioanacleto.speakeazy.core.database.SpeakEazyRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SpeakEazyRoomDatabase::class.java,
            SpeakEazyRoomDatabase::class.java.simpleName
        ).build()
    }
    factory { get<SpeakEazyRoomDatabase>().ingredientDao() }
    factory { get<SpeakEazyRoomDatabase>().favoritesDao() }
    factory { get<SpeakEazyRoomDatabase>().userDao() }
    factory { get<SpeakEazyRoomDatabase>().createCocktailDao() }
}