package com.zioanacleto.speakeazy.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.zioanacleto.speakeazy.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.database.dao.IngredientDao
import com.zioanacleto.speakeazy.database.dao.UserDao
import com.zioanacleto.speakeazy.database.entities.FavoriteEntity
import com.zioanacleto.speakeazy.database.entities.IngredientEntity
import com.zioanacleto.speakeazy.database.entities.UserEntity

@Database(
    entities = [
        IngredientEntity::class,
        FavoriteEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class SpeakEazyRoomDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: SpeakEazyRoomDatabase? = null

        fun getDatabase(context: Context): SpeakEazyRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpeakEazyRoomDatabase::class.java,
                    "SpeakEazyRoomDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}