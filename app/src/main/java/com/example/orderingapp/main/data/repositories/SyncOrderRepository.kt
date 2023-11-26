package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncOrderRepository(
    private val firestore: FirebaseFirestore,
    private val dao: OrderingAppDao
) : SyncOrderUseCase {
    override fun syncOrderRemote(order: Order): Flow<ApiResult<Boolean>> = flow {
        val result = safeRequestSuspend {
            firestore.collection("orders").add(order).isSuccessful
        }
        emit(result)
    }

    override fun syncOrderLocal(order: Order): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            dao.updateOrderSync(order.id)
        }
        emit(result)
    }
}