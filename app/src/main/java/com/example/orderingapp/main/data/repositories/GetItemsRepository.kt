package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.entities.Item
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetItemsRepository(private val firestore: FirebaseFirestore) : GetItemsUseCase {
    private val items = mutableListOf<Item>()

    override fun getItems(): Flow<ApiResult<List<Item>>> = callbackFlow {
        val listener =
            firestore.collection("items").addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(ApiResult.Error(exception))
                    return@addSnapshotListener
                }
                try {
                    snapshot?.documentChanges?.map { doc ->
                        when (doc.type) {
                            DocumentChange.Type.ADDED -> doc.document.let {
                                items.add(
                                    mapItem(it)
                                )
                            }
                            DocumentChange.Type.MODIFIED -> doc.document.let {
                                items.run {
                                    val find = find { item -> it.id == item.id }
                                    set(
                                        indexOf(find), mapItem(it)
                                    )
                                }
                            }
                            DocumentChange.Type.REMOVED -> doc.document.let {
                                items.remove(mapItem(it))
                            }
                        }
                    }
                    trySend(ApiResult.Success(items))
                } catch (ex: Exception) {
                    trySend(ApiResult.Error(ex))
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    private fun mapItem(it: QueryDocumentSnapshot) = Item(
        id = it.id,
        description = it["description"] as String,
        currentValue = it["currentValue"] as Double,
        minimumStock = it["minimumStock"] as Int,
        currentStock = it["currentStock"] as Int
    )
}