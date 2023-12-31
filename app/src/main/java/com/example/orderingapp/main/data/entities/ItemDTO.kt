package com.example.orderingapp.main.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ItemDTO(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val currentValue: Double,
    val costValue: Double,
    val minimumStock: Int,
    val currentStock: Int
)