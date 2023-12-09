package com.example.orderingapp.main.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    val isSyncing = mutableStateOf(false)
    private val _unsyncedOrders = mutableStateMapOf<String, Order>()
    val unsyncedOrders: Map<String, Order> = _unsyncedOrders

    private var _items = mutableStateMapOf<String, ItemCompose>()
    val items: Map<String, ItemCompose> = _items

    private var hasCalledGetUnsyncedOrders = false

    init {
        viewModelScope.launch {
            observeMenuItems()
        }
    }

    private suspend fun observeMenuItems() {
        mainUseCases.getItemsUseCase.getItemsFromRemote().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    _items.clear()
                    _items.putAll(result.data)
                    if (!hasCalledGetUnsyncedOrders) {
                        getUnsyncedOrders()
                        hasCalledGetUnsyncedOrders = !hasCalledGetUnsyncedOrders
                    }
                }
                is ApiResult.Error -> _items.clear()
            }
        }
    }

    private suspend fun getUnsyncedOrders() {
        mainUseCases.getOrdersUseCase.getUnsyncedOrders(_items.toMap()).firstOrNull { result ->
            when (result) {
                is ApiResult.Success -> {
                    _unsyncedOrders.clear()
                    _unsyncedOrders.putAll(result.data)
                    true
                }
                is ApiResult.Error -> {
                    _unsyncedOrders.clear()
                    false
                }
            }
        }
    }

    fun setUnsyncedOrders(entry: OrderEntry) {
        _unsyncedOrders[entry.key] = entry.value
    }

    fun startSyncing() {
        viewModelScope.launch {
            isSyncing.value = true
            mainUseCases.syncOrderUseCase.syncOrderRemote(_unsyncedOrders).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        updateOrdersLocal()
                    }
                    is ApiResult.Error -> {
                        isSyncing.value = false
                    }
                }
            }
        }
    }

    private suspend fun updateOrdersLocal() {
        mainUseCases.syncOrderUseCase.syncOrderLocal(_unsyncedOrders).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    _unsyncedOrders.clear()
                    isSyncing.value = false
                }
                is ApiResult.Error -> {
                    isSyncing.value = false
                }
            }
        }
    }

    fun clearAddedItems() {
        viewModelScope.launch {
            _items.filter { it.value.quantity.value > 0 }.forEach { it.value.quantity.value = 0 }
        }
    }
}