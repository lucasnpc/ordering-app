package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface InsertOrderUseCase {
    fun insertOrderRemote(order: Order): Flow<ApiResult<Boolean>>
    fun insertOrderLocal(order: Order): Flow<ApiResult<Unit>>
}