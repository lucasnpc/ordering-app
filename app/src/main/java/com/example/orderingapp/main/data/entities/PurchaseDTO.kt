package com.example.orderingapp.main.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.orderingapp.main.data.entities.converters.MapItemIdTypeConverter
import java.util.UUID

@Entity
@TypeConverters(MapItemIdTypeConverter::class)
data class PurchaseDTO(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val items: Map<String, Int> = mapOf(),
    val date: String,
    val hour: String,
    val purchaseValue: Double,
    val paymentWay: String,
    var synced: Boolean = false
)
