package com.example.orderingapp.commons.extensions

import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun jsonToOrder() {
        val order = Order(date = "", hour = "", orderValue = 0.0, paymentWay = "")
        val orderJson = Gson().toJson(order)
        assertThat(orderJson.jsonToOrder()).isEqualTo(order)
    }

    @Test
    fun jsonToOrderException() {
        val invalidJson = Item()
        val orderJson = Gson().toJson(invalidJson)
        assertThat(orderJson.jsonToOrder()).isNull()
    }
}