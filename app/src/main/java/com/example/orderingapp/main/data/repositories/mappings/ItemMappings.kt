package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose

fun Item.toItemDTO(): ItemDTO {
    return ItemDTO(
        id = id,
        description = description,
        currentValue = currentValue,
        minimumStock = minimumStock,
        currentStock = currentStock
    )
}


fun List<ItemDTO>.fromDTOToListItemCompose(): List<ItemCompose> {
    return this.map { itemDTO ->
        ItemCompose(
            Item(
                id = itemDTO.id,
                description = itemDTO.description,
                currentValue = itemDTO.currentValue,
                minimumStock = itemDTO.minimumStock,
                currentStock = itemDTO.currentStock,
            )
        )
    }
}