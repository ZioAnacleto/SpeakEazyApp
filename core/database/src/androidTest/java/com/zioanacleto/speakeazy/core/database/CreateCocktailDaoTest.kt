package com.zioanacleto.speakeazy.core.database

import com.zioanacleto.speakeazy.core.database.entities.CreateCocktailEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateCocktailDaoTest : AbstractDatabaseTest() {

    @Test
    fun test_getAll_returnsAllCreateCocktailEntities() {
        // given
        insertCreateCocktailEntities()

        // when
        val response = createCocktailDao.getAll()

        // then
        assertEquals(
            response.map { it.uniqueId },
            listOf(1, 2, 3)
        )
    }

    @Test
    fun test_delete_deletesCreateCocktailEntityWithGivenId() {
        // given
        insertCreateCocktailEntities()
        val uniqueId = 2

        // when
        createCocktailDao.delete(uniqueId)
        val entities = createCocktailDao.getAll()

        // then
        assert(entities.firstOrNull { it.uniqueId == uniqueId } == null)
    }

    private fun insertCreateCocktailEntities() {
        createCocktailDao.upsert(
            CreateCocktailEntity(
                uniqueId = 1,
                currentStep = 0,
                createdTime = 0L,
                lastUpdateTime = 0L
            )
        )
        createCocktailDao.upsert(
            CreateCocktailEntity(
                uniqueId = 2,
                currentStep = 0,
                createdTime = 0L,
                lastUpdateTime = 0L
            )
        )
        createCocktailDao.upsert(
            CreateCocktailEntity(
                uniqueId = 3,
                currentStep = 0,
                createdTime = 0L,
                lastUpdateTime = 0L
            )
        )
    }
}