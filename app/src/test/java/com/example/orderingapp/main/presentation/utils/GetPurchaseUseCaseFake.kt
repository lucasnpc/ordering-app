package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.usecase.GetPurchasesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetPurchaseUseCaseFake(private val testException: RuntimeException? = null) :
    GetPurchasesUseCase {
    private val listPurchases = TestData().purchases
    override fun getUnsyncedPurchases(): Flow<ApiResult<Map<String, Purchase>>> {
        val result = safeRequest {
            if (testException != null)
                throw testException
            listPurchases
        }
        return flowOf(result)
    }
}