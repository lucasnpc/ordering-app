package com.example.orderingapp.main.domain.model

data class Purchase(
    val items: Map<String, Item> = mapOf(),
    val date: String,
    val hour: String,
    val purchaseValue: Double,
    val paymentWay: String,
)
