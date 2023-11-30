package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.ItemDTO
import com.google.firebase.firestore.QueryDocumentSnapshot

fun QueryDocumentSnapshot.documentToItemDTO() = ItemDTO(
    id = id,
    description = this["description"] as String,
    currentValue = this["currentValue"].toString().toDouble(),
    minimumStock = this["minimumStock"].toString().toInt(),
    currentStock = this["currentStock"].toString().toInt()
)