package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.UpdateItemsStockUseCase
import kotlinx.coroutines.flow.Flow

class UpdateItemsStockUseCaseFake: UpdateItemsStockUseCase {
    override fun updateItemsStock(items: Map<String, ItemCompose>): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }
}