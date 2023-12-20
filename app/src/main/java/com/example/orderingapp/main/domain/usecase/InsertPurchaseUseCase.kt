package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import kotlinx.coroutines.flow.Flow

interface InsertPurchaseUseCase {
    fun insertPurchaseLocal(purchase: Purchase): Flow<ApiResult<PurchaseEntry>>
}