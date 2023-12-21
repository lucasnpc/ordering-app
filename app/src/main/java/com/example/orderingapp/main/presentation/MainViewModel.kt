package com.example.orderingapp.main.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    val isSyncing = mutableStateOf(false)
    private val _unsyncedOrders = mutableStateMapOf<String, Order>()
    val unsyncedOrders: Map<String, Order> = _unsyncedOrders

    private val _unsyncedPurchases = mutableStateMapOf<String, Purchase>()
    val unsyncedPurchases: Map<String, Purchase> = _unsyncedPurchases

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
                    insertItems(result)
                    putItems(result.data)
                }

                is ApiResult.Error -> getItemsFromLocal()
            }
        }
    }

    private fun insertItems(result: ApiResult.Success<Map<String, ItemCompose>>) {
        viewModelScope.launch {
            mainUseCases.insertItemsUseCase.insertItem(result.data).firstOrNull()
        }
    }

    private suspend fun getUnsyncedOrders() {
        mainUseCases.getOrdersUseCase.getUnsyncedOrders().firstOrNull { result ->
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

    private suspend fun getUnsyncedPurchases() {
        mainUseCases.getPurchasesUseCase.getUnsyncedPurchases().firstOrNull { result ->
            when (result) {
                is ApiResult.Success -> {
                    _unsyncedPurchases.clear()
                    _unsyncedPurchases.putAll(result.data)
                    true
                }

                is ApiResult.Error -> {
                    _unsyncedPurchases.clear()
                    false
                }
            }
        }
    }

    private suspend fun getItemsFromLocal() {
        mainUseCases.getItemsUseCase.getItemsFromLocal().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    putItems(result.data)
                }

                is ApiResult.Error -> {
                    _items.clear()
                }
            }
        }
    }

    private suspend fun putItems(data: Map<String, ItemCompose>) {
        _items.clear()
        _items.putAll(data)
        if (!hasCalledGetUnsyncedOrders) {
            getUnsyncedOrders()
            getUnsyncedPurchases()
            hasCalledGetUnsyncedOrders = !hasCalledGetUnsyncedOrders
        }
    }

    fun setUnsyncedOrder(entry: OrderEntry) {
        _unsyncedOrders[entry.key] = entry.value
    }

    fun setUnsyncedPurchase(entry: PurchaseEntry) {
        _unsyncedPurchases[entry.key] = entry.value
    }

    fun startSyncing() {
        isSyncing.value = true

        viewModelScope.launch {
            _unsyncedOrders.forEach { updateItemsStock(it.value.items) }
            syncOrderRemote()
            _unsyncedPurchases.forEach { updateItemsStock(it.value.items) }
            syncPurchaseRemote()

            isSyncing.value = false
        }
    }

    private suspend fun syncOrderRemote() {
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

    private suspend fun syncPurchaseRemote() {
        viewModelScope.launch {
            mainUseCases.syncPurchaseUseCase.syncPurchaseRemote(_unsyncedPurchases)
                .collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            updatePurchasesLocal()
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
                }

                is ApiResult.Error -> {
                    isSyncing.value = false
                }
            }
        }
    }

    private suspend fun updatePurchasesLocal() {
        mainUseCases.syncPurchaseUseCase.syncPurchaseLocal(_unsyncedPurchases).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    _unsyncedPurchases.clear()
                }

                is ApiResult.Error -> {
                    isSyncing.value = false
                }
            }
        }
    }

    private suspend fun updateItemsStock(items: Map<String, Item>) {
        mainUseCases.updateItemsStockUseCase.updateItemsStock(items).collect()
    }

    fun clearAddedItems() {
        viewModelScope.launch {
            _items.filter { it.value.quantity.value > 0 }.forEach { it.value.quantity.value = 0 }
        }
    }
}