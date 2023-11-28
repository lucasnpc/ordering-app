package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.usecase.InsertItemsUseCase
import kotlinx.coroutines.flow.Flow

class InsertItemsUseCaseFake: InsertItemsUseCase {
    override fun insertItem(items: List<Item>): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }
}