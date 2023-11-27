package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.mappings.fromOrderDTOToOrder
import com.example.orderingapp.commons.mappings.toOrderDTO
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.InsertOrderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InsertOrderRepository(
    private val orderingAppDao: OrderingAppDao
) : InsertOrderUseCase {

    override fun insertOrderLocal(order: Order, _items: List<Item>): Flow<ApiResult<List<Order>>> =
        flow {
            val result = safeRequestSuspend {
                orderingAppDao.insertOrder(order.toOrderDTO())
                orderingAppDao.getUnsyncedOrders().fromOrderDTOToOrder(_items)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}