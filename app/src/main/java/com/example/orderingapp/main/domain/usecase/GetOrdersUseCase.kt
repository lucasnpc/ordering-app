package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface GetOrdersUseCase {
    fun getOrders(): Flow<ApiResult<Map<String, Order>>>
    fun getUnsyncedOrders(): Flow<ApiResult<Map<String, Order>>>
}