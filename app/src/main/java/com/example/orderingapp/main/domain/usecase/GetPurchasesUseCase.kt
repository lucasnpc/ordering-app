package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import kotlinx.coroutines.flow.Flow

interface GetPurchasesUseCase {
    fun getUnsyncedPurchases(): Flow<ApiResult<Map<String, Purchase>>>
}