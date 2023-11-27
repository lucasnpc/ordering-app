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
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    private val _unsyncedOrders = mutableStateListOf<Order>()
    val unsyncedOrders: List<Order> = _unsyncedOrders

    private val _items = mutableStateListOf<Item>()
    val items: List<Item> = _items

    init {
        viewModelScope.launch {
            mainUseCases.getItemsUseCase.getItemsFromRemote().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _items.clear()
                        _items.run {
                            result.data.forEach { item ->
                                find { it.id == item.id }?.let {
                                    set(indexOf(it), item)
                                } ?: _items.add(item)
                            }
                        }
                    }
                    is ApiResult.Error -> Unit
                }
            }
            mainUseCases.getOrdersUseCase.getUnsyncedOrders(_items).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _unsyncedOrders.addAll(result.data)
                    }
                    is ApiResult.Error -> Unit
                }
            }
        }
    }
}