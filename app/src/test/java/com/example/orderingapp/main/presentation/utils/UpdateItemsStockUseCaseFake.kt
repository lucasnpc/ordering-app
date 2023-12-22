package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.usecase.UpdateItemsStockUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UpdateItemsStockUseCaseFake : UpdateItemsStockUseCase {
    override fun updateItemsStock(items: Map<String, Item>): Flow<ApiResult<Unit>> {
        val result = safeRequest { }
        return flowOf(result)
    }
}