package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.usecase.SyncPurchaseUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

class SyncPurchaseRepository(
    private val firestore: FirebaseFirestore,
    private val dao: OrderingAppDao
) : SyncPurchaseUseCase {
    override fun syncPurchaseRemote(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>> =
        flow {
            val latch = CountDownLatch(purchases.size)
            val result = safeRequestSuspend {
                purchases.forEach { purchase ->
                    firestore.collection(FirestoreCollections.purchases).document(purchase.key)
                        .set(purchase.value)
                        .addOnCompleteListener { latch.countDown() }
                }
                withContext(Dispatchers.IO) {
                    latch.await()
                }
            }
            emit(result)
        }

    override fun syncPurchaseLocal(purchases: Map<String, Purchase>): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            purchases.forEach { purchase ->
                dao.updatePurchaseSync(purchase.key)
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}