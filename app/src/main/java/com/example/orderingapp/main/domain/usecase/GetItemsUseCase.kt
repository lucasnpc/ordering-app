package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.entities.Item
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {
    fun getItems(): Flow<ApiResult<List<Item>>>
}