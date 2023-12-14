package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import kotlinx.coroutines.flow.Flow

interface InsertItemsUseCase {
    fun insertItem(items: Map<String, ItemCompose>): Flow<ApiResult<Unit>>
}