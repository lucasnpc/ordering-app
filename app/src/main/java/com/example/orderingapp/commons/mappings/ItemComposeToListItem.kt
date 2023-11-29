package com.example.orderingapp.commons.mappings

import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose

fun List<ItemCompose>.composeToListItem(): List<Item> {
    return this.map { it.item }
}