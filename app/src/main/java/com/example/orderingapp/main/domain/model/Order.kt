package com.example.orderingapp.main.domain.model

import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: Map<String, Item> = mapOf(),
    val date: String,
    val hour: String,
    val orderValue: Double,
    val paymentWay: String,
)
