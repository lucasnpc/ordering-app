package com.example.orderingapp.commons.extensions

import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.jsonToOrderEntry(): OrderEntry? {
    return try {
        val orderType =
            object : TypeToken<OrderEntry>() {}.type
        Gson().fromJson<OrderEntry>(this, orderType).let {
            OrderEntry(
                it.key, Order(
                    items = it.value.items,
                    date = it.value.date,
                    hour = it.value.hour,
                    orderValue = it.value.orderValue,
                    paymentWay = it.value.paymentWay
                )
            )
        }
    } catch (ex: Exception) {
        return null
    }
}