package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {
    fun getItemsFromRemote(): Flow<ApiResult<Map<String, ItemCompose>>>

    fun getItemsFromLocal(): Flow<ApiResult<Map<String, ItemCompose>>>
}