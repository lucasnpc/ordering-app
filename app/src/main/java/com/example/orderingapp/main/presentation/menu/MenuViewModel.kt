package com.example.orderingapp.main.presentation.menu

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.extensions.toDateFormat
import com.example.orderingapp.commons.extensions.toHourFormat
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
                    is ApiResult.Error -> Unit
                }
            }
        }
    }

    fun insertOrder(paymentWay: String, unsyncedOrdersCallback: (List<Order>) -> Unit) {
        viewModelScope.launch {
            val addedItems = _items.filter { it.quantity.value > 0 }
            mainUseCases.insertOrderUseCase.insertOrderLocal(
                Order(
                    items = addedItems,
                    hour = System.currentTimeMillis().toHourFormat(),
                    date = System.currentTimeMillis().toDateFormat(),
                    orderValue = addedItems.sumOf { it.currentValue * it.quantity.value },
                    paymentWay = paymentWay
                ),
                _items
            ).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _items.filter { it.quantity.value > 0 }.forEach { addedItem ->
                            addedItem.quantity.value = 0
                            addedItem.finalQuantity = addedItem.quantity.value
                        }
                        unsyncedOrdersCallback(result.data)
                    }
                    is ApiResult.Error -> Unit
                }
            }
        }
    }
}