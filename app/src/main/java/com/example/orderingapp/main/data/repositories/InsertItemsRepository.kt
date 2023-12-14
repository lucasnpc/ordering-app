package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.toItemDTO
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.InsertItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InsertItemsRepository(private val dao: OrderingAppDao) : InsertItemsUseCase {
    override fun insertItem(items: Map<String, ItemCompose>): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            items.forEach { item ->
                dao.insertItem(item.toItemDTO())
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}
