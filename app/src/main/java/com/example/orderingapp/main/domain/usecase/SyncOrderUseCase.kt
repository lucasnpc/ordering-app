package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface SyncOrderUseCase {
    fun syncOrderRemote(orders: List<Order>): Flow<ApiResult<Unit>>
    fun syncOrderLocal(orders: List<Order>): Flow<ApiResult<Unit>>
}