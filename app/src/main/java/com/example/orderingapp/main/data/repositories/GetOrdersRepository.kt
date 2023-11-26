package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.mappings.fromOrderDTOToOrder
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrdersRepository(private val dao: OrderingAppDao) : GetOrdersUseCase {
    override fun getOrders(_items: List<Item>): Flow<ApiResult<List<Order>>> = flow {
        val result = safeRequestSuspend {
            dao.getOrders().fromOrderDTOToOrder(_items)
        }
        emit(result)
    }
}