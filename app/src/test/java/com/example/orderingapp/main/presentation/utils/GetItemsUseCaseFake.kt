package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetItemsUseCaseFake(
    private val remoteTestException: RuntimeException? = null,
    private val localTestException: RuntimeException? = null
) : GetItemsUseCase {
    private val list = TestData().itemsCompose

    override fun getItemsFromRemote(): Flow<ApiResult<Map<String, ItemCompose>>> {
        val result = safeRequest {
            if (remoteTestException != null)
                throw remoteTestException
            list
        }
        return flowOf(result)
    }

    override fun getItemsFromLocal(): Flow<ApiResult<Map<String, ItemCompose>>> {
        val result = safeRequest {
            if (localTestException != null)
                throw localTestException
            list
        }
        return flowOf(result)
    }
}