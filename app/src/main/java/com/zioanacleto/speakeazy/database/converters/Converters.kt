package com.zioanacleto.speakeazy.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import java.util.Date

/**
 *  This converter will be useful when we want for example to return a list of wizards
 *  ordered by date
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class CreateWizardStepConverter {
    @TypeConverter
    fun fromCreateWizardStep(value: CreateWizardStepData): Int {
        return value.order
    }

    @TypeConverter
    fun toCreateWizardStep(value: Int): CreateWizardStepData {
        return CreateWizardStepData::class.sealedSubclasses
            .map { it.objectInstance!! }
            .first { it.order == value }
    }
}

class MapConverter {
    @TypeConverter
    fun fromMap(map: Map<String, String>): String = Gson().toJson(map)

    @TypeConverter
    fun toMap(json: String): Map<String, String> =
        Gson().fromJson(json, Map::class.java) as Map<String, String>
}