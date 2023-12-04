package com.example.orderingapp.main.presentation.menu

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
class MenuViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    fun insertOrder(
        order: Order,
        _items: Map<String, ItemCompose>,
        unsyncedOrdersCallback: (List<Order>) -> Unit
    ) {
        viewModelScope.launch {
            mainUseCases.insertOrderUseCase.insertOrderLocal(order, _items).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        cleanItems(_items)
                        unsyncedOrdersCallback(result.data)
                    }
                    is ApiResult.Error -> {
                        cleanItems(_items)
                        unsyncedOrdersCallback(listOf())
                    }
                }
            }
        }
    }

    private fun cleanItems(_items: Map<String, ItemCompose>) {
        _items.filter { it.value.quantity.value > 0 }.forEach {
            it.value.quantity.value = 0
        }
    }
}