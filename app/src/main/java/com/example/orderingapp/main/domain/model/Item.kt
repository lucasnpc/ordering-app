package com.example.orderingapp.main.domain.model

data class Item(
    val description: String = "",
    val currentValue: Double = 0.0,
    val costValue: Double = 0.0,
    val minimumStock: Int = 0,
    val currentStock: Int = 0,
    var finalQuantity: Int = 0,
)
