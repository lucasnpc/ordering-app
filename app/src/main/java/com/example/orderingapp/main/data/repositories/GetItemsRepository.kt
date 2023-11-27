package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequest
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class GetItemsRepository(
    private val firestore: FirebaseFirestore,
    private val dao: OrderingAppDao
) : GetItemsUseCase {
    private val itemDTO = mutableListOf<ItemDTO>()

    override fun getItemsFromRemote(): Flow<ApiResult<List<Item>>> = callbackFlow {
        val listener =
            firestore.collection("items").addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(ApiResult.Error(exception))
                    return@addSnapshotListener
                }
                val result = safeRequest {
                    snapshot?.documentChanges?.map { doc ->
                        when (doc.type) {
                            DocumentChange.Type.ADDED -> doc.document.let {
                                itemDTO.add(
                                    it.documentToItemDTO()
                                )
                            }
                            DocumentChange.Type.MODIFIED -> doc.document.let {
                                itemDTO.run {
                                    val find = find { item -> it.id == item.id }
                                    set(
                                        indexOf(find), it.documentToItemDTO()
                                    )
                                }
                            }
                            DocumentChange.Type.REMOVED -> doc.document.let {
                                itemDTO.remove(it.documentToItemDTO())
                            }
                        }
                    }
                    itemDTO.fromDTOToListItem()
                }
                trySend(result)
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun getItemsFromLocal(): Flow<ApiResult<List<Item>>> = flow {
        val result = safeRequestSuspend {
            dao.getItems().fromDTOToListItem()
        }
        emit(result)
    }

    private fun QueryDocumentSnapshot.documentToItemDTO() = ItemDTO(
        id = id,
        description = this["description"] as String,
        currentValue = this["currentValue"].toString().toDouble(),
        minimumStock = this["minimumStock"].toString().toInt(),
        currentStock = this["currentStock"].toString().toInt()
    )

    private fun List<ItemDTO>.fromDTOToListItem(): List<Item> {
        return this.map { itemDTO ->
            Item(
                id = itemDTO.id,
                description = itemDTO.description,
                currentValue = itemDTO.currentValue,
                minimumStock = itemDTO.minimumStock,
                currentStock = itemDTO.currentStock,
                quantity = 0
            )
        }
    }
}
