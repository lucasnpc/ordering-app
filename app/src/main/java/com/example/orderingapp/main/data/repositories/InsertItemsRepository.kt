package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.usecase.InsertItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertItemsRepository(private val dao: OrderingAppDao) : InsertItemsUseCase {
    override fun insertItem(items: List<Item>): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            items.forEach { item ->
                dao.insertItem(item.toItemDTO())
            }
        }
        emit(result)
    }

}

fun Item.toItemDTO(): ItemDTO {
    return ItemDTO(
        id = id,
        description = description,
        currentValue = currentValue,
        minimumStock = minimumStock,
        currentStock = currentStock
    )
}
