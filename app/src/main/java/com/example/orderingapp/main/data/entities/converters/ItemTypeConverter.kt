package com.example.orderingapp.main.data.entities.converters

import androidx.room.TypeConverter
import com.example.orderingapp.main.data.entities.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromItemsMap(map: Map<String, Int>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toItemsMap(value: String): Map<String, Int> {
        val mapType = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(value, mapType)
    }
}
