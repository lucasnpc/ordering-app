package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.fromPurchaseDTOListToOrderMap
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.usecase.GetPurchasesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetPurchasesRepository(private val dao: OrderingAppDao) : GetPurchasesUseCase {
    override fun getUnsyncedPurchases(): Flow<ApiResult<Map<String, Purchase>>> = flow {
        val result = safeRequestSuspend {
            dao.getUnsyncedPurchases().fromPurchaseDTOListToOrderMap()
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}