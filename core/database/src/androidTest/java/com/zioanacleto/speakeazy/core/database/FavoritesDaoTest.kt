package com.zioanacleto.speakeazy.core.database

import com.zioanacleto.speakeazy.core.database.entities.FavoriteEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoritesDaoTest : AbstractDatabaseTest() {

    @Test
    fun test_getAll_shouldReturnAllAvailableFavorites() = runTest {
        // given
        loadFavorites()

        // when
        val favorites = favoritesDao.getAll()

        // then
        assertEquals(
            listOf(1, 2, 3),
            favorites.map { it.id }
        )
    }

    @Test
    fun test_loadAllByIds_shouldReturnAllAvailableFavoritesIds() = runTest {
        // given
        loadFavorites()
        val ids = intArrayOf(1, 3)

        // when
        val favorites = favoritesDao.loadAllByIds(ids)

        // then
        assertEquals(
            ids.toList(),
            favorites.map { it.id }
        )
    }

    @Test
    fun test_delete_shouldDeleteFavorite() {
        // given
        loadFavorites()
        val favoriteId = 2

        // when
        favoritesDao.delete(favoriteId)
        val favorites = favoritesDao.getAll()

        // then
        assert(favorites.size == 2)
        assert(favorites.firstOrNull { it.id == favoriteId } == null)
    }

    private fun loadFavorites() {
        favoritesDao.insert(
            FavoriteEntity(
                id = 1,
                name = "testFavorite1"
            ),
            FavoriteEntity(
                id = 2,
                name = "testFavorite2"
            ),
            FavoriteEntity(
                id = 3,
                name = "testFavorite3"
            )
        )
    }
}