package com.example.orderingapp.main.domain.model

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val currentValue: Double,
    val minimumStock: Int,
    val currentStock: Int,
    var quantity: Int
)
