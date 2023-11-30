package com.example.orderingapp.main.presentation.order

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OrderViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    private val _orders = mutableStateListOf<Order>()
    val orders: List<Order> = _orders

    fun getOrders(list: List<ItemCompose>) {
        viewModelScope.launch {
            mainUseCases.getOrdersUseCase.getOrders(list).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _orders.clear()
                        _orders.addAll(result.data.reversed())
                    }
                    is ApiResult.Error -> _orders.clear()
                }
            }
        }
    }
}