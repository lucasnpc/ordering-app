package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.main.data.entities.Order
import kotlinx.coroutines.flow.Flow

interface SyncOrderUseCase {
    fun syncOrder(order: Order): Flow<Boolean>
}