package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.usecase.SyncPurchaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SyncPurchaseUseCaseFake(
    private val remoteException: RuntimeException? = null,
    private val localException: RuntimeException? = null
) : SyncPurchaseUseCase {
    override fun syncPurchaseRemote(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>> {
        val result = safeRequest {
            if (remoteException != null)
                throw remoteException
            Unit
        }
        return flowOf(result)
    }

    override fun syncPurchaseLocal(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>> {
        val result = safeRequest {
            if (localException != null)
                throw localException
            Unit
        }
        return flowOf(result)
    }
}