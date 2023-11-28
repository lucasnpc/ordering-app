package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrdersUseCaseFake : GetOrdersUseCase {
    override fun getOrders(_items: List<Item>): Flow<ApiResult<List<Order>>> {
        TODO("Not yet implemented")
    }

    override fun getUnsyncedOrders(_items: List<Item>): Flow<ApiResult<List<Order>>> {
        return flow { emit(ApiResult.Success(listOf())) }
    }
}