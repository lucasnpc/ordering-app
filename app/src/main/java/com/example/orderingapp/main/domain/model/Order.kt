package com.example.orderingapp.main.domain.model

import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: List<Item> = listOf(),
    val date: String,
    val hour: String,
    val orderValue: Double,
    val paymentWay: String,
    val synced: Boolean = false
)
