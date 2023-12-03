package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetOrdersUseCaseFake(private val testException: RuntimeException? = null) : GetOrdersUseCase {
    private val listOrders = TestData().orders

    override fun getOrders(_items: Map<String, ItemCompose>): Flow<ApiResult<List<Order>>> {
        TODO("Not yet implemented")
    }

    override fun getUnsyncedOrders(_items: Map<String, ItemCompose>): Flow<ApiResult<List<Order>>> {
        val result = safeRequest {
            if (testException != null)
                throw testException
            listOrders
        }
        return flowOf(result)
    }
}