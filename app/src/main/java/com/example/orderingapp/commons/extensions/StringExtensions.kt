package com.example.orderingapp.commons.extensions

import com.example.orderingapp.main.domain.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.convertToOrder(): Order? {
    return try {
        val orderType =
            object : TypeToken<Order>() {}.type
        Gson().fromJson(this, orderType)
    } catch (ex: Exception) {
        return null
    }
}