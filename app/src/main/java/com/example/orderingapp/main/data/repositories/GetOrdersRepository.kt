package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.fromOrderDTOListToOrderMap
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetOrdersRepository(private val dao: OrderingAppDao) : GetOrdersUseCase {
    override fun getOrders(): Flow<ApiResult<Map<String, Order>>> =
        flow {
            val result = safeRequestSuspend {
                dao.getOrders().also { list ->
                    if (list.any { it.items.isEmpty() })
                        list.filter { it.items.isEmpty() }.forEach { dao.deleteOrder(it) }
                }.fromOrderDTOListToOrderMap().filter { it.value.items.isNotEmpty() }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)

    override fun getUnsyncedOrders(): Flow<ApiResult<Map<String, Order>>> =
        flow {
            val result = safeRequestSuspend {
                dao.getUnsyncedOrders().fromOrderDTOListToOrderMap()
                    .filter { it.value.items.isNotEmpty() }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}