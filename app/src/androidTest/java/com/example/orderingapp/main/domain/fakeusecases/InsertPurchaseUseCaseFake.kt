package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.domain.usecase.InsertPurchaseUseCase
import kotlinx.coroutines.flow.Flow

class InsertPurchaseUseCaseFake: InsertPurchaseUseCase {
    override fun insertPurchaseLocal(purchase: Purchase): Flow<ApiResult<PurchaseEntry>> {
        TODO("Not yet implemented")
    }
}