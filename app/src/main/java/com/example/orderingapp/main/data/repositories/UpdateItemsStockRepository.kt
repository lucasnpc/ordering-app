package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.repositories.mappings.itemToDocument
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.usecase.UpdateItemsStockUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateItemsStockRepository(private val firestore: FirebaseFirestore) :
    UpdateItemsStockUseCase {
    override fun updateItemsStock(items: Map<String, Item>): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            items.forEach {
                firestore.collection(FirestoreCollections.items).document(it.key)
                    .set(it.value.itemToDocument())
            }
        }
        emit(result)
    }
}