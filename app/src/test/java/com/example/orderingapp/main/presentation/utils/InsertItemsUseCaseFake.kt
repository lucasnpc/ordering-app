package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.InsertItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class InsertItemsUseCaseFake : InsertItemsUseCase {
    private val list: MutableMap<String, ItemCompose> = mutableMapOf()
    override fun insertItem(items: Map<String, ItemCompose>): Flow<ApiResult<Unit>> {
        val result = safeRequest {
            list.putAll(items)
        }
        return flowOf(result)
    }
}