package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.fromOrderDTOToOrder
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetOrdersRepository(private val dao: OrderingAppDao) : GetOrdersUseCase {
    override fun getOrders(_items: Map<String, ItemCompose>): Flow<ApiResult<List<Order>>> = flow {
        val result = safeRequestSuspend {
            dao.getOrders().fromOrderDTOToOrder(_items)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun getUnsyncedOrders(_items: Map<String, ItemCompose>): Flow<ApiResult<List<Order>>> = flow {
        val result = safeRequestSuspend {
            dao.getUnsyncedOrders().fromOrderDTOToOrder(_items)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}