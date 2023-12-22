package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import kotlinx.coroutines.flow.Flow

interface SyncPurchaseUseCase {
    fun syncPurchaseRemote(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>>
    fun syncPurchaseLocal(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>>
}