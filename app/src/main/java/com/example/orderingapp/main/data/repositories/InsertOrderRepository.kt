package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.mappings.toOrderDTO
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.usecase.InsertOrderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InsertOrderRepository(
    private val dao: OrderingAppDao
) : InsertOrderUseCase {

    override fun insertOrderLocal(
        order: Order,
    ): Flow<ApiResult<OrderEntry>> =
        flow {
            val result = safeRequestSuspend {
                val orderDTO = order.toOrderDTO()
                dao.insertOrder(orderDTO)
                OrderEntry(orderDTO.id, order)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}