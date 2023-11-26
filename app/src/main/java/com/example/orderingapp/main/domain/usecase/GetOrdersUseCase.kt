package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.entities.OrderDTO
import kotlinx.coroutines.flow.Flow

interface GetOrdersUseCase {
    fun getOrders(): Flow<ApiResult<List<OrderDTO>>>
    fun getUnsyncedOrders(): Flow<ApiResult<List<OrderDTO>>>
}