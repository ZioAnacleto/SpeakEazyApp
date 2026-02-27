package com.zioanacleto.speakeazy.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zioanacleto.speakeazy.core.domain.search.model.QueryModel

@Entity
data class SearchQueryEntity(
    @PrimaryKey val name: String,
    val lastUsed: Long
)

fun SearchQueryEntity.toModel() = QueryModel(
    query = name,
    lastUsed = lastUsed
)