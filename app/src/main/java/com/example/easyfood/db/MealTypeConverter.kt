package com.example.easyfood.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {
    @TypeConverter
    fun fromAnyToString(attribute: Any?): String {
        return attribute?.toString() ?: ""
    }

    @TypeConverter
    fun fromStringToAny(attribute: String?): Any {
        return attribute ?: ""
    }
}