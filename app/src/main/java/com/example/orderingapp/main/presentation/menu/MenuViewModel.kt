package com.example.orderingapp.main.presentation.menu

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
class MenuViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    private val _items = mutableStateListOf<Item>()
    val items: List<Item> = _items

    init {
        viewModelScope.launch {
            mainUseCases.getItemsUseCase.getItemsFromRemote().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _items.clear()
                        _items.addAll(result.data)
                    }
                    is ApiResult.Error -> _items.clear()
                }
            }
        }
    }

    fun insertOrder(order: Order, unsyncedOrdersCallback: (List<Order>) -> Unit) {
        viewModelScope.launch {
            mainUseCases.insertOrderUseCase.insertOrderLocal(order, _items).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        cleanItems()
                        unsyncedOrdersCallback(result.data)
                    }
                    is ApiResult.Error -> {
                        cleanItems()
                        unsyncedOrdersCallback(listOf())
                    }
                }
            }
        }
    }

    private fun cleanItems() {
        _items.filter { it.finalQuantity > 0 }.forEach { addedItem ->
            addedItem.quantity.value = 0
            addedItem.finalQuantity = addedItem.quantity.value

        }
    }
}