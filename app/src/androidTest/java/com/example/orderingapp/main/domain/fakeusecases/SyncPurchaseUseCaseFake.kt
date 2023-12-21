package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.usecase.SyncPurchaseUseCase
import kotlinx.coroutines.flow.Flow

class SyncPurchaseUseCaseFake : SyncPurchaseUseCase {

    override fun syncPurchaseRemote(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }

    override fun syncPurchaseLocal(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }
}