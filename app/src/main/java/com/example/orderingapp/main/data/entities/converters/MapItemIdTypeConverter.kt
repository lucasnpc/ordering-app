package com.example.orderingapp.main.data.entities.converters

import androidx.room.TypeConverter
import com.example.orderingapp.main.domain.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapItemIdTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): Map<String, Item> {
        val mapType = object: TypeToken<Map<String, Item>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: Map<String, Item>): String {
        return gson.toJson(map)
    }
}
