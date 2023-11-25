package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequest
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.Order
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncOrderRepository(
    private val dao: OrderingAppDao,
    private val firestore: FirebaseFirestore
) : SyncOrderUseCase {
    override fun syncOrder(order: Order): Flow<Boolean> = flow {
        val result = safeRequest {
            firestore.collection("orders").add(order).isSuccessful
        }
        when (result) {
            is ApiResult.Success -> {
                dao.updateOrderSync(order.id)
                emit(result.data)
            }
            is ApiResult.Error -> throw result.exception
        }
    }
}