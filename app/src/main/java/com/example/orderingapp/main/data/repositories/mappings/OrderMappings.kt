package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order

fun List<OrderDTO>.fromOrderDTOToOrder(_items: List<Item>): List<Order> {
    return this.map { orderDTO ->
        Order(
            id = orderDTO.id,
            items = orderDTO.items.toListItem(_items),
            date = orderDTO.date,
            hour = orderDTO.hour,
            orderValue = orderDTO.orderValue,
            paymentWay = orderDTO.paymentWay,
        )
    }
}

fun Map<String, Int>.toListItem(_items: List<Item>): List<Item> {
    val list = this.map {
        _items.find { item -> item.id == it.key }?.let { find ->
            Item(
                id = it.key,
                description = find.description,
                currentValue = find.currentValue,
                minimumStock = find.minimumStock,
                currentStock = find.currentStock,
                finalQuantity = it.value,
            )
        } ?: Item()
    }

    return list.filter { it.finalQuantity > 0 }
}

fun Order.toOrderDTO(): OrderDTO {
    return OrderDTO(
        id = id,
        items = items.associate { it.id to it.finalQuantity },
        date = date,
        hour = hour,
        orderValue = orderValue,
        paymentWay = paymentWay,
    )
}

fun List<ItemCompose>.composeToListItem(): List<Item> {
    return this.map { it.item }
}