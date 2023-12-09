package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import kotlinx.coroutines.flow.Flow

interface InsertOrderUseCase {
    fun insertOrderLocal(
        order: Order,
    ): Flow<ApiResult<OrderEntry>>
}