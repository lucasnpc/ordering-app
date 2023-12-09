package com.example.orderingapp.main.domain.model

data class Order(
    val items: Map<String, Item> = mapOf(),
    val date: String,
    val hour: String,
    val orderValue: Double,
    val paymentWay: String,
)
