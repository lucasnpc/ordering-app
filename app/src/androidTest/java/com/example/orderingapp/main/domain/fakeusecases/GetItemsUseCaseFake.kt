package com.example.orderingapp.main.domain.fakeusecases

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetItemsUseCaseFake: GetItemsUseCase {
    override fun getItemsFromRemote(): Flow<ApiResult<Map<String, ItemCompose>>> {
        return flow { emit(ApiResult.Success(mapOf())) }
    }

    override fun getItemsFromLocal(): Flow<ApiResult<Map<String, ItemCompose>>> {
        TODO("Not yet implemented")
    }

}