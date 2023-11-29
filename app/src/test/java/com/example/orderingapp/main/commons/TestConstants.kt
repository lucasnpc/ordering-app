package com.example.orderingapp.main.commons

import androidx.compose.runtime.mutableStateOf
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order

object TestConstants {
    const val testMsgException = "messageException"
    val testException = RuntimeException(testMsgException)

    val listItems = listOf(
        Item(
            id = "1",
            description = "item 1",
            finalQuantity = 2
        ), Item(
            id = "2",
            description = "item 2",
            finalQuantity = 2
        )
    )
    val listOrder = listOf(
        OrderDTO(
            id = "123",
            items = mapOf("1" to 2),
            date = "21 Dec 2021",
            hour = "12:00:00",
            orderValue = 10.0,
            paymentWay = "Pix",
            synced = false
        )
    )
    val order = Order(
        id = "123",
        items = listOf(
            Item(
                id = "1",
                description = "item 1",
                finalQuantity = 2
            )
        ),
        date = "21 Dec 2021",
        hour = "12:00:00",
        paymentWay = "Pix",
        orderValue = 10.0,
    )
    val orderDTO = OrderDTO(
        id = "123",
        items = mapOf("1" to 2),
        date = "21 Dec 2021",
        hour = "12:00:00",
        orderValue = 10.0,
        paymentWay = "Pix",
        synced = false
    )
    val item = Item(
        id = "1",
        description = "item 1",
    )
    val itemModify = Item(
        id = item.id,
        description = "${item.description} modificado",
        currentValue = item.currentValue,
        minimumStock = item.minimumStock,
        currentStock = item.currentStock,
        finalQuantity = 0
    )
    val itemDTO = ItemDTO(
        id = "1",
        description = "item 1",
        currentValue = 0.0,
        minimumStock = 0,
        currentStock = 0,
    )
}