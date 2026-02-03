package com.zioanacleto.speakeazy.core.database

import com.zioanacleto.speakeazy.core.database.entities.IngredientEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientDaoTest : AbstractDatabaseTest() {

    @Test
    fun test_getAll_shouldReturnAllIngredients() {
        // given
        insertIngredients()

        // when
        val ingredients = ingredientsDao.getAll()

        // then
        assertEquals(
            listOf(1, 2, 3),
            ingredients.map { it.id }
        )
    }

    @Test
    fun test_loadAllByIds_shouldReturnIngredientsWithGivenIds() {
        // given
        insertIngredients()
        val ids = intArrayOf(1, 3)

        // when
        val ingredients = ingredientsDao.loadAllByIds(ids)

        // then
        assertEquals(
            ids.toList(),
            ingredients.map { it.id }
        )
    }

    private fun insertIngredients() {
        ingredientsDao.insertAll(
            IngredientEntity(
                id = 1,
                name = "testIngredient1",
                imageUrl = "testImageUrl1"
            ),
            IngredientEntity(
                id = 2,
                name = "testIngredient2",
                imageUrl = "testImageUrl2"
            ),
            IngredientEntity(
                id = 3,
                name = "testIngredient3",
                imageUrl = "testImageUrl3"
            )
        )
    }
}