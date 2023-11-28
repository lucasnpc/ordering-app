package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetItemsUseCaseFake: GetItemsUseCase {
    override fun getItemsFromRemote(): Flow<ApiResult<List<Item>>> {
        return flow { emit(ApiResult.Success(listOf())) }
    }

    override fun getItemsFromLocal(): Flow<ApiResult<List<Item>>> {
        TODO("Not yet implemented")
    }

}