package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.google.firebase.firestore.QueryDocumentSnapshot

fun QueryDocumentSnapshot.documentToItemCompose() = ItemCompose(
    Item(
        description = this["description"] as String,
        currentValue = this["currentValue"].toString().toDouble(),
        costValue = this["costValue"].toString().toDouble(),
        minimumStock = this["minimumStock"].toString().toInt(),
        currentStock = this["currentStock"].toString().toInt()
    )
)

fun Item.itemToDocument() = hashMapOf(
    "description" to description,
    "currentValue" to currentValue,
    "costValue" to costValue,
    "minimumStock" to minimumStock,
    "currentStock" to currentStock
)