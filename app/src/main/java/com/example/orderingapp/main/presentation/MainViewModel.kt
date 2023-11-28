package com.example.orderingapp.main.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    val isSyncing = mutableStateOf(false)
    private val _unsyncedOrders = mutableStateListOf<Order>()
    val unsyncedOrders: List<Order> = _unsyncedOrders

    private val _items = mutableStateListOf<ItemCompose>()
    val items: List<ItemCompose> = _items

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

    private suspend fun getUnsyncedOrders() {
        mainUseCases.getOrdersUseCase.getUnsyncedOrders(_items).first { result ->
            when (result) {
                is ApiResult.Success -> {
                    _unsyncedOrders.clear()
                    _unsyncedOrders.addAll(result.data)
                    true
                }
                is ApiResult.Error -> {
                    _unsyncedOrders.clear()
                    false
                }
            }
        }
    }

    fun setUnsyncedOrders(orders: List<Order>) {
        _unsyncedOrders.clear()
        _unsyncedOrders.addAll(orders)
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

}