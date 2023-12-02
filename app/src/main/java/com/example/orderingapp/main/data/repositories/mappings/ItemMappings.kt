package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose

fun Item.toItemDTO(): ItemDTO {
    return ItemDTO(
        description = description,
        currentValue = currentValue,
        minimumStock = minimumStock,
        currentStock = currentStock
    )
}

fun List<ItemDTO>.fromDTOToListItemCompose(): Map<String, ItemCompose> {
    return this.associateBy({ it.id }, {
        ItemCompose(
            Item(
                description = it.description,
                currentValue = it.currentValue,
                minimumStock = it.minimumStock,
                currentStock = it.currentStock,
            )
        )
    })
}
