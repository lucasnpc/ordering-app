package com.example.orderingapp.main.presentation.utils.mappings

import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose

fun Map<String, ItemCompose>.composeToItem(): Map<String, Item> {
    return this.mapValues { entry ->
        Item(
            description = entry.value.item.description,
            currentValue = entry.value.item.currentValue,
            minimumStock = entry.value.item.minimumStock,
            currentStock = entry.value.item.currentStock,
            finalQuantity = entry.value.quantity.value
        )
    }
}
