package com.example.orderingapp.main.presentation.order

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OrderViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    private val _orders = mutableStateMapOf<String, Order>()
    val orders: Map<String, Order> = _orders

    fun getOrders(list: Map<String, ItemCompose>) {
        viewModelScope.launch {
            mainUseCases.getOrdersUseCase.getOrders(list).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _orders.clear()
                        _orders.putAll(result.data)
                    }
                    is ApiResult.Error -> _orders.clear()
                }
            }
        }
    }

    fun toLocalDateTime(date: String, hour: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
        return LocalDateTime.parse("$date $hour", formatter)
    }
}