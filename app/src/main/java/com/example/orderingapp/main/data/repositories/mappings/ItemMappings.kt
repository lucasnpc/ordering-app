package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose


fun Map.Entry<String, ItemCompose>.toItemDTO(): ItemDTO {
    return ItemDTO(
        id = key,
        description = value.item.description,
        currentValue = value.item.currentValue,
        costValue = value.item.costValue,
        minimumStock = value.item.minimumStock,
        currentStock = value.item.currentStock
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
