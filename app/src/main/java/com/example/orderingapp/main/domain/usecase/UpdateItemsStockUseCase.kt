package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface UpdateItemsStockUseCase {
    fun updateItemsStock(items: Map<String, Item>): Flow<ApiResult<Unit>>
}