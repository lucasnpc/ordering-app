package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.main.data.entities.Order
import kotlinx.coroutines.flow.Flow

interface GetOrdersUseCase {
    fun getOrders(): Flow<List<Order>>
}