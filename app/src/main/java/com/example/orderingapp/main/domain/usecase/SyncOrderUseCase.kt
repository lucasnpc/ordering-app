package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface SyncOrderUseCase {
    fun syncOrderRemote(order: Order): Flow<ApiResult<Boolean>>
    fun syncOrderLocal(order: Order): Flow<ApiResult<Unit>>
}