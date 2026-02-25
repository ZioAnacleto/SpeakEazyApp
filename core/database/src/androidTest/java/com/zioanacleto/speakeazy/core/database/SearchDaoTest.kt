package com.zioanacleto.speakeazy.core.database

import com.zioanacleto.speakeazy.core.database.entities.SearchQueryEntity
import org.junit.Test

class SearchDaoTest : AbstractDatabaseTest() {

    @Test
    fun test_getRecentSearches_returnsRecentSearches() {
        // given
        insertSearchQueries()

        // when
        val queries = searchDao.getRecentSearches()

        // then
        assert(queries.map { it.name } == listOf("test2", "test3", "test1"))
    }

    @Test
    fun test_upsertSearch_willInsertNewSearch() {
        // given
        insertSearchQueries()
        val newSearch = SearchQueryEntity(
            name = "test4",
            lastUsed = 10000L
        )

        // when
        searchDao.upsertSearch(newSearch)
        val queries = searchDao.getRecentSearches()

        // then
        assert(queries.map { it.name } == listOf("test4", "test2", "test3", "test1"))
    }

    @Test
    fun test_upsertSearch_willUpdateExistingSearch() {
        // given
        insertSearchQueries()
        val newSearch = SearchQueryEntity(
            name = "test1",
            lastUsed = 10000L
        )

        // when
        searchDao.upsertSearch(newSearch)
        val queries = searchDao.getRecentSearches()

        // then
        assert(queries.map { it.name } == listOf("test1", "test2", "test3"))
    }

    @Test
    fun test_deleteSearch_deletesSearch() {
        // given
        insertSearchQueries()
        val name = "test2"

        //when
        searchDao.deleteSearch(name)
        val queries = searchDao.getRecentSearches()

        //then
        assert(queries.map { it.name } == listOf("test3", "test1"))
    }

    @Test
    fun test_deleteQueriesOlderThan_deletesOldQueries() {
        // given
        insertSearchQueries()
        val threshold = 110L

        // when
        searchDao.deleteQueriesOlderThan(threshold)
        val queries = searchDao.getRecentSearches()

        // then
        assert(queries.map { it.name } == listOf("test2"))
    }

    private fun insertSearchQueries() {
        with(searchDao) {
            upsertSearch(
                SearchQueryEntity(
                    name = "test1",
                    lastUsed = 10L
                )
            )
            upsertSearch(
                SearchQueryEntity(
                    name = "test2",
                    lastUsed = 1000L
                )
            )
            upsertSearch(
                SearchQueryEntity(
                    name = "test3",
                    lastUsed = 100L
                )
            )
        }
    }
}