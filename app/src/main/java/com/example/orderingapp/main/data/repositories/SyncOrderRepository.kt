package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SyncOrderRepository(
    private val firestore: FirebaseFirestore,
    private val dao: OrderingAppDao
) : SyncOrderUseCase {
    override fun syncOrderRemote(orders: List<Order>): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            orders.forEach { order ->
                firestore.collection(FirestoreCollections.orders).document(order.id).set(order)
            }
        }
        emit(result)
    }

    override fun syncOrderLocal(orders: List<Order>): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            orders.forEach {order ->
                dao.updateOrderSync(order.id)
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}