package com.example.orderingapp.main.presentation.order

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    private val _orders = mutableStateMapOf<String, Order>()
    val orders: Map<String, Order> = _orders

    fun getOrders() {
        viewModelScope.launch {
            mainUseCases.getOrdersUseCase.getOrders().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _orders.clear()
                        _orders.putAll(result.data)
                    }

                    is ApiResult.Error -> {
                        _orders.clear()
                    }
                }
            }
        }
    }
}