package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface GetOrdersUseCase {
    fun getOrders(_items: List<Item>): Flow<ApiResult<List<Order>>>
    fun getUnsyncedOrders(_items: List<Item>): Flow<ApiResult<List<Order>>>
}