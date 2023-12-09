package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.usecase.InsertOrderUseCase
import kotlinx.coroutines.flow.Flow

class InsertOrderUseCaseFake : InsertOrderUseCase {

    override fun insertOrderLocal(
        order: Order,
        _items: Map<String, ItemCompose>
    ): Flow<ApiResult<OrderEntry>> {
        TODO("Not yet implemented")
    }

}