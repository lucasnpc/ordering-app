package com.example.orderingapp.commons.extensions

import com.example.orderingapp.main.domain.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.jsonToOrder(): Order? {
    return try {
        val orderType =
            object : TypeToken<Order>() {}.type
        Gson().fromJson<Order?>(this, orderType).let {
            Order(
                id = it.id,
                items = it.items,
                date = it.date,
                hour = it.hour,
                orderValue = it.orderValue,
                paymentWay = it.paymentWay
            )
        }
    } catch (ex: Exception) {
        return null
    }
}