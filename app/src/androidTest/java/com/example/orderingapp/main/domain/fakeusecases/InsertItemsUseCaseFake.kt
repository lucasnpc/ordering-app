package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.InsertItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class InsertItemsUseCaseFake : InsertItemsUseCase {

    override fun insertItem(items: Map<String, ItemCompose>): Flow<ApiResult<Unit>> {
        return flowOf(ApiResult.Success(Unit))
    }
}