package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.usecase.GetPurchasesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPurchaseUseCaseFake: GetPurchasesUseCase {
    override fun getUnsyncedPurchases(): Flow<ApiResult<Map<String, Purchase>>> {
        return flow { emit(ApiResult.Success(mapOf())) }
    }
}