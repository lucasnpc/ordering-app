package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order

fun List<OrderDTO>.fromOrderDTOToOrder(_items: Map<String, ItemCompose>): List<Order> {
    return this.map { orderDTO ->
        Order(
            id = orderDTO.id,
            items = orderDTO.items.mapToOrderItem(_items),
            date = orderDTO.date,
            hour = orderDTO.hour,
            orderValue = orderDTO.orderValue,
            paymentWay = orderDTO.paymentWay,
        )
    }
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
        id = id,
        items = items.mapValues { it.value.finalQuantity },
        date = date,
        hour = hour,
        orderValue = orderValue,
        paymentWay = paymentWay,
    )
}
