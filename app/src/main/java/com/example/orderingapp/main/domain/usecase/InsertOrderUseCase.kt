package com.example.orderingapp.main.domain.usecase

import com.example.orderingapp.main.data.entities.Order

interface InsertOrderUseCase {
    fun insertOrder(order: Order)
}