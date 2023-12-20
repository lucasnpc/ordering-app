package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Order

fun List<OrderDTO>.fromOrderDTOListToOrderMap(): Map<String, Order> {
    return this.associateBy({ it.id }, {
        Order(
            items = it.items,
            date = it.date,
            hour = it.hour,
            orderValue = it.orderValue,
            paymentWay = it.paymentWay,
        )
    })
}

fun Order.toOrderDTO(): OrderDTO {
    return OrderDTO(
        items = items,
        date = date,
        hour = hour,
        orderValue = orderValue,
        paymentWay = paymentWay,
    )
}
