package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import kotlinx.coroutines.flow.Flow

class SyncOrderUseCaseFake: SyncOrderUseCase {
    override fun syncOrderRemote(order: Order): Flow<ApiResult<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun syncOrderLocal(order: Order): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }
}