package com.example.orderingapp.main.commons

import androidx.compose.runtime.mutableStateOf
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry

data class TestData(
    private val item1: Pair<String, Item> =
        "1" to Item(
            description = "item 1",
            currentValue = 1.0,
            finalQuantity = 2
        ),
    private val item2: Pair<String, Item> =
        "2" to Item(
            description = "item 2",
            currentValue = 1.0,
            finalQuantity = 2
        ),
    private val itemDTO1: ItemDTO = ItemDTO(
        id = "1",
        description = "item 1",
        currentValue = 1.0,
        minimumStock = 0,
        currentStock = 0,
    ),
    private val itemDTO2: ItemDTO = ItemDTO(
        id = "2",
        description = "item 2",
        currentValue = 1.0,
        minimumStock = 0,
        currentStock = 0,
    ),
    private val order1: OrderEntry = OrderEntry(
        key = "1", value = Order(
            items = mapOf(item1),
            date = "21 Dec 2021", hour = "12:00:00", paymentWay = "Pix", orderValue = 10.0,
        )
    ),
    private val order2: OrderEntry = OrderEntry(
        key = "2", value = Order(
            items = mapOf(item1, item2),
            date = "21 Dec 2021", hour = "12:00:00", paymentWay = "Pix", orderValue = 10.0,
        )
    ),
    private val orderDTO1: OrderDTO = OrderDTO(
        id = "1",
        items = mapOf("1" to 2),
        date = "21 Dec 2021",
        hour = "12:00:00",
        orderValue = 10.0,
        paymentWay = "Pix",
        synced = false
    ),
    private val orderDTO2: OrderDTO = OrderDTO(
        id = "2",
        items = mapOf("1" to 2, "2" to 2),
        date = "21 Dec 2021",
        hour = "12:00:00",
        orderValue = 10.0,
        paymentWay = "Pix",
        synced = false
    ),
    private val purchase1: PurchaseEntry = PurchaseEntry(
        "1", Purchase(
            items = mapOf(item1, item2),
            date = "21 Dec 2021", hour = "12:00:00", paymentWay = "Pix", purchaseValue = 10.0,
        )
    ),
    private val purchase2: PurchaseEntry = PurchaseEntry(
        "2", Purchase(
            items = mapOf(item1, item2),
            date = "21 Dec 2021", hour = "12:00:00", paymentWay = "Pix", purchaseValue = 10.0,
        )
    ),
    val itemsDTO: Map<String, ItemDTO> = mapOf("1" to itemDTO1, "2" to itemDTO2),
    val itemsCompose: Map<String, ItemCompose> = mapOf(
        item1.first to ItemCompose(
            item1.second,
            mutableStateOf(item1.second.finalQuantity)
        ),
        item2.first to ItemCompose(
            item2.second,
            mutableStateOf(item2.second.finalQuantity)
        )
    ),
    val ordersDTO: List<OrderDTO> = listOf(orderDTO1, orderDTO2),
    val orders: Map<String, Order> = mapOf(order1.key to order1.value, order2.key to order2.value),
    val purchases: Map<String, Purchase> = mapOf(
        purchase1.key to purchase1.value,
        purchase2.key to purchase2.value
    )
)
