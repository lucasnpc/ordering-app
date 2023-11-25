package com.example.orderingapp.main.domain.usecase

import kotlinx.coroutines.flow.Flow

interface InsertItemsUseCase {
    fun insertItem(): Flow<Result<Unit>>
}