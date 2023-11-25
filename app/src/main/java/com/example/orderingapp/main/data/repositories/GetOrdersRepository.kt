package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequest
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrdersRepository(private val dao: OrderingAppDao) : GetOrdersUseCase {
    override fun getOrders(): Flow<List<Order>> = flow {
        val result = safeRequest {
            dao.getOrders()
        }
        when (result) {
            is ApiResult.Success -> emit(result.data)
            is ApiResult.Error -> throw result.exception
        }
    }
}