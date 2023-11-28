package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface InsertOrderUseCase {
    fun insertOrderLocal(order: Order, _items: List<ItemCompose>): Flow<ApiResult<List<Order>>>
}