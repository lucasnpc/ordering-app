package com.example.orderingapp.main.data.utils

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
            description = "item 1"
        ), Item(id = "2", description = "item 2")
    )
    val listOrder = listOf(
        OrderDTO(
            id = "123",
            items = mapOf("1" to 2), dateHour = "12:00:00 21/12/2021",
            orderValue = 10.0,
            synced = false
        )
    )
    val order = Order(
        id = "123",
        items = listOf(
            Item(
                id = "1",
                description = "item 1",
                quantity = mutableStateOf(2)
            )
        ),
        date = "21/12/2021",
        hour = "12:00:00",
        orderValue = 10.0,
    )
    val orderDTO = OrderDTO(
        id = "123",
        items = mapOf("1" to 2),
        dateHour = "12:00:00 21/12/2021",
        orderValue = 10.0,
        synced = false
    )
    val item = Item(
        id = "123",
        description = "testeItem",
        currentValue = 10.0,
        minimumStock = 5,
        currentStock = 10,
        quantity = mutableStateOf(0)
    )
    val itemModify = Item(
        id = item.id,
        description = "${item.description} modificado",
        currentValue = item.currentValue,
        minimumStock = item.minimumStock,
        currentStock = item.currentStock,
        quantity = mutableStateOf(0)
    )
    val itemDTO = ItemDTO(
        id = "123",
        description = "testeItem",
        currentValue = 10.0,
        minimumStock = 5,
        currentStock = 10,
    )
}