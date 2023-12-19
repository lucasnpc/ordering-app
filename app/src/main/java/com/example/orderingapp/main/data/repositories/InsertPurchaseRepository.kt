package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.toPurchaseDTO
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.domain.usecase.InsertPurchaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InsertPurchaseRepository(
    private val dao: OrderingAppDao
) : InsertPurchaseUseCase {
    override fun insertPurchaseLocal(purchase: Purchase): Flow<ApiResult<PurchaseEntry>> = flow {
        val result = safeRequestSuspend {
            val purchaseDTO = purchase.toPurchaseDTO()
            dao.insertPurchase(purchaseDTO)
            PurchaseEntry(purchaseDTO.id, purchase)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}