package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.documentToItemDTO
import com.example.orderingapp.main.data.repositories.mappings.fromDTOToListItemCompose
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetItemsRepository(
    private val firestore: FirebaseFirestore,
    private val dao: OrderingAppDao
) : GetItemsUseCase {
    private val itemsCompose = mutableMapOf<String, ItemCompose>()

    override fun getItemsFromRemote(): Flow<ApiResult<Map<String, ItemCompose>>> = callbackFlow {
        val listener =
            firestore.collection(FirestoreCollections.items)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        trySend(ApiResult.Error(exception))
                        return@addSnapshotListener
                    }
                    val result = safeRequest {
                        snapshot?.documentChanges?.map { doc ->
                            when (doc.type) {
                                DocumentChange.Type.ADDED -> doc.document.let {
                                    itemsCompose[it.id] = it.documentToItemDTO()

                                }
                                DocumentChange.Type.MODIFIED -> doc.document.let {
                                    itemsCompose[it.id] = it.documentToItemDTO()
                                }
                                DocumentChange.Type.REMOVED -> doc.document.let {
                                    itemsCompose.remove(it.id)
                                }
                            }
                        }
                        itemsCompose
                    }
                    trySend(result)
                }
        awaitClose {
            listener.remove()
        }
    }

    override fun getItemsFromLocal(): Flow<ApiResult<Map<String, ItemCompose>>> = flow {
        val result = safeRequestSuspend {
            dao.getItems().fromDTOToListItemCompose()
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}
