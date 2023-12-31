package com.example.orderingapp.commons.extensions

import com.example.orderingapp.main.presentation.utils.extensions.jsonToOrderEntry
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun jsonToOrder() {
        val order = OrderEntry("1", Order(date = "", hour = "", orderValue = 0.0, paymentWay = ""))
        val orderJson = Gson().toJson(order)
        assertThat(orderJson.jsonToOrderEntry()).isEqualTo(order)
    }

    @Test
    fun jsonToOrderException() {
        val invalidJson = Item()
        val orderJson = Gson().toJson(invalidJson)
        assertThat(orderJson.jsonToOrderEntry()).isNull()
    }
}