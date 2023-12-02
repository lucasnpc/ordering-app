package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.google.firebase.firestore.QueryDocumentSnapshot

fun QueryDocumentSnapshot.documentToItemDTO() = ItemCompose(
    Item(
        description = this["description"] as String,
        currentValue = this["currentValue"].toString().toDouble(),
        minimumStock = this["minimumStock"].toString().toInt(),
        currentStock = this["currentStock"].toString().toInt()
    )
)