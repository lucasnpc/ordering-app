package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order

fun List<OrderDTO>.fromOrderDTOListToOrderMap(_items: Map<String, ItemCompose>): Map<String, Order> {
    return this.associateBy({ it.id }, {
        Order(
            items = it.items.mapToOrderItem(_items),
            date = it.date,
            hour = it.hour,
            orderValue = it.orderValue,
            paymentWay = it.paymentWay,
        )
    })
}

private fun Map<String, Int>.mapToOrderItem(_items: Map<String, ItemCompose>): Map<String, Item> {
    return this.mapValues { entry ->
        _items[entry.key]?.let {
            Item(
                description = it.item.description,
                currentValue = it.item.currentValue,
                minimumStock = it.item.minimumStock,
                currentStock = it.item.currentStock,
                finalQuantity = entry.value
            )
        } ?: Item()
    }
}

fun Order.toOrderDTO(): OrderDTO {
    return OrderDTO(
        items = items.mapValues { it.value.finalQuantity },
        date = date,
        hour = hour,
        orderValue = orderValue,
        paymentWay = paymentWay,
    )
}
