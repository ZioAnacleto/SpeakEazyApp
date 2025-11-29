package com.zioanacleto.speakeazy.database

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

object DatabaseMigrations {

    @RenameColumn.Entries(
        RenameColumn(
            tableName = "CreateCocktailEntity",
            fromColumnName = "latestStep",
            toColumnName = "currentStep"
        )
    )
    class Schema4to5 : AutoMigrationSpec

    @RenameColumn.Entries(
        RenameColumn(
            tableName = "CreateCocktailEntity",
            fromColumnName = "id",
            toColumnName = "uniqueId"
        )
    )
    class Schema7to8 : AutoMigrationSpec
}