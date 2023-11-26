package com.example.orderingapp.commons.mappings

import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order

fun List<OrderDTO>.fromOrderDTOToOrder(_items: List<Item>): List<Order> {
    return this.map { orderDTO ->
        Order(
            id = orderDTO.id,
            items = orderDTO.items.toListItem(_items),
            date = orderDTO.dateHour.split(" ")[1],
            hour = orderDTO.dateHour.split(" ")[0],
            orderValue = orderDTO.orderValue,
            synced = orderDTO.synced
        )
    }
}

fun Map<String, Int>.toListItem(_items: List<Item>): List<Item> {
    val list = this.map {
        _items.find { item -> item.id == it.key }?.let { find ->
            Item(
                id = find.id,
                description = find.description,
                currentValue = find.currentValue,
                minimumStock = find.minimumStock,
                currentStock = find.currentStock,
                quantity = it.value
            )
        } ?: Item()
    }

    return list.filter { it.quantity > 0 }
}

fun Order.toOrderDTO(): OrderDTO {
    return OrderDTO(
        id = id,
        items = items.associate { it.id to it.quantity },
        dateHour = "$hour $date",
        orderValue = orderValue,
        synced = synced
    )
}