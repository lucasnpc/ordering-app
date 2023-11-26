package com.example.orderingapp.main.domain.model

data class Order(
    val id: String,
    val items: List<Item> = listOf(),
    val date: String,
    val hour: String,
    val orderValue: Double,
)
