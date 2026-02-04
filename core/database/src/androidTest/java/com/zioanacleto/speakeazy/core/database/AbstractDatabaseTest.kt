package com.zioanacleto.speakeazy.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.zioanacleto.speakeazy.core.database.dao.CreateCocktailDao
import com.zioanacleto.speakeazy.core.database.dao.FavoritesDao
import com.zioanacleto.speakeazy.core.database.dao.IngredientDao
import com.zioanacleto.speakeazy.core.database.dao.UserDao
import org.junit.After
import org.junit.Before

abstract class AbstractDatabaseTest {

    private lateinit var database: SpeakEazyRoomDatabase
    protected lateinit var createCocktailDao: CreateCocktailDao
    protected lateinit var favoritesDao: FavoritesDao
    protected lateinit var ingredientsDao: IngredientDao
    protected lateinit var userDao: UserDao

    @Before
    fun setup() {
        database = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                SpeakEazyRoomDatabase::class.java
            ).build()
        }

        createCocktailDao = database.createCocktailDao()
        favoritesDao = database.favoritesDao()
        ingredientsDao = database.ingredientDao()
        userDao = database.userDao()
    }

    @After
    fun tearDown() = database.close()
}