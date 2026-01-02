package com.zioanacleto.speakeazy.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zioanacleto.speakeazy.database.converters.CreateWizardStepConverter
import com.zioanacleto.speakeazy.database.converters.DateConverter
import com.zioanacleto.speakeazy.database.converters.MapConverter
import com.zioanacleto.speakeazy.database.dao.CreateCocktailDao
import com.zioanacleto.speakeazy.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.database.dao.IngredientDao
import com.zioanacleto.speakeazy.database.dao.UserDao
import com.zioanacleto.speakeazy.database.entities.CreateCocktailEntity
import com.zioanacleto.speakeazy.database.entities.FavoriteEntity
import com.zioanacleto.speakeazy.database.entities.IngredientEntity
import com.zioanacleto.speakeazy.database.entities.UserEntity

@Database(
    entities = [
        IngredientEntity::class,
        FavoriteEntity::class,
        UserEntity::class,
        CreateCocktailEntity::class
    ],
    version = 11,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = DatabaseMigrations.Schema4to5::class),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8, spec = DatabaseMigrations.Schema7to8::class),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11)
    ]
)
@TypeConverters(DateConverter::class, CreateWizardStepConverter::class, MapConverter::class)
abstract class SpeakEazyRoomDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun userDao(): UserDao
    abstract fun createCocktailDao(): CreateCocktailDao
}