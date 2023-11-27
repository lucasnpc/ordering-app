package com.example.orderingapp.main.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    private val _unsyncedOrders = mutableStateListOf<Order>()
    val unsyncedOrders: List<Order> = _unsyncedOrders

    private val _items = mutableStateListOf<Item>()
    val items: List<Item> = _items

    init {
        viewModelScope.launch {
            observeMenuItems()
            getUnsyncedOrders()
        }
    }

    private suspend fun observeMenuItems() {
        mainUseCases.getItemsUseCase.getItemsFromRemote().first { result ->
            when (result) {
                is ApiResult.Success -> {
                    _items.clear()
                    _items.addAll(result.data)
                    true
                }
                is ApiResult.Error -> false
            }
        }
    }

    private fun getUnsyncedOrders() {
        viewModelScope.launch {
            mainUseCases.getOrdersUseCase.getUnsyncedOrders(_items).first { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _unsyncedOrders.clear()
                        _unsyncedOrders.addAll(result.data)
                        true
                    }
                    is ApiResult.Error -> false
                }
            }
        }
    }

    fun setUnsyncedOrders(orders: List<Order>) {
        _unsyncedOrders.clear()
        _unsyncedOrders.addAll(orders)
    }
}