package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrdersUseCaseFake : GetOrdersUseCase {
    override fun getOrders(_items: Map<String, ItemCompose>): Flow<ApiResult<List<Order>>> {
        TODO("Not yet implemented")
    }

    override fun getUnsyncedOrders(_items: Map<String, ItemCompose>): Flow<ApiResult<List<Order>>> {
        return flow { emit(ApiResult.Success(listOf())) }
    }

}