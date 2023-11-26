package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.main.data.entities.OrderDTO
import kotlinx.coroutines.flow.Flow

interface SyncOrderUseCase {
    fun syncOrder(orderDTO: OrderDTO): Flow<Boolean>
}