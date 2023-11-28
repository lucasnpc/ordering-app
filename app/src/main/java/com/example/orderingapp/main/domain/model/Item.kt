package com.example.orderingapp.main.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val description: String = "",
    val currentValue: Double = 0.0,
    val minimumStock: Int = 0,
    val currentStock: Int = 0,
    var finalQuantity: Int = 0,
    @Transient
    var quantity: MutableState<Int> = mutableStateOf(0)
)
