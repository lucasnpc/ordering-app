package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {
    fun getItemsFromRemote(): Flow<ApiResult<List<Item>>>

    fun getItemsFromLocal(): Flow<ApiResult<List<Item>>>
}