package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.main.data.repositories.mappings.composeToListItem
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.data.repositories.mappings.fromOrderDTOToOrder
import com.example.orderingapp.main.data.repositories.mappings.toOrderDTO
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.InsertOrderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InsertOrderRepository(
    private val orderingAppDao: OrderingAppDao
) : InsertOrderUseCase {

    override fun insertOrderLocal(order: Order, _items: List<ItemCompose>): Flow<ApiResult<List<Order>>> =
        flow {
            val result = safeRequestSuspend {
                orderingAppDao.insertOrder(order.toOrderDTO())
                orderingAppDao.getUnsyncedOrders().fromOrderDTOToOrder(_items.composeToListItem())
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}