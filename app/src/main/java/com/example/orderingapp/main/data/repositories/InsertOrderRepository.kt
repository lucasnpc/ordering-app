package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.InsertOrderUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertOrderRepository(
    private val orderingAppDao: OrderingAppDao
) : InsertOrderUseCase {

    override fun insertOrderLocal(order: Order): Flow<ApiResult<Unit>> = flow {
        val result = safeRequestSuspend {
            orderingAppDao.insertOrder(order.toOrderDTO())
        }
        emit(result)
    }

    private fun Order.toOrderDTO(): OrderDTO {
        return OrderDTO(
            id = id,
            items = items.associate { it.id to it.quantity },
            dateHour = "$hour $date",
            orderValue = orderValue
        )
    }
}