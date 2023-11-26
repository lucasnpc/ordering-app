package com.example.orderingapp.main.domain.model

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val description: String = "",
    val currentValue: Double = 0.0,
    val minimumStock: Int = 0,
    val currentStock: Int = 0,
    var quantity: Int = 0
)
