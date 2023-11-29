package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SyncOrderUseCaseFake(
    private val remoteException: RuntimeException? = null,
    private val localException: RuntimeException? = null
) : SyncOrderUseCase {

    override fun syncOrderRemote(orders: List<Order>): Flow<ApiResult<Unit>> {
        val result = safeRequest {
            if (remoteException != null)
                throw remoteException
            Unit
        }
        return flowOf(result)
    }

    override fun syncOrderLocal(orders: List<Order>): Flow<ApiResult<Unit>> {
        val result = safeRequest {
            if (localException != null)
                throw localException
            Unit
        }
        return flowOf(result)
    }
}