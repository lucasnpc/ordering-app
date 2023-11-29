package com.example.orderingapp.main.presentation.utils

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.request.safeRequest
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetItemsUseCaseFake(private val testException: RuntimeException? = null) : GetItemsUseCase {
    private val list = TestData().itemsCompose

    override fun getItemsFromRemote(): Flow<ApiResult<List<ItemCompose>>> {
        val result = safeRequest {
            if (testException != null)
                throw testException
            list
        }
        return flowOf(result)
    }

    override fun getItemsFromLocal(): Flow<ApiResult<List<ItemCompose>>> {
        TODO("Not yet implemented")
    }


}