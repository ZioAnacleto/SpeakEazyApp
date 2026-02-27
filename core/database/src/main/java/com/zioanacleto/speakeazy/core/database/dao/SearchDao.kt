package com.zioanacleto.speakeazy.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zioanacleto.speakeazy.core.database.entities.SearchQueryEntity

@Dao
interface SearchDao {
    @Query("SELECT * FROM SearchQueryEntity ORDER BY lastUsed DESC LIMIT 20")
    fun getRecentSearches(): List<SearchQueryEntity>

    @Upsert
    fun upsertSearch(searchQuery: SearchQueryEntity)

    @Query("DELETE FROM SearchQueryEntity WHERE name = :name")
    fun deleteSearch(name: String)

    @Query("DELETE FROM SearchQueryEntity WHERE lastUsed < :threshold")
    fun deleteQueriesOlderThan(threshold: Long)
}