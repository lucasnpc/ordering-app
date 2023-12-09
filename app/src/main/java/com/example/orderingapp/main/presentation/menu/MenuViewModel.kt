package com.example.orderingapp.main.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    fun insertOrder(
        order: Order,
        _items: Map<String, ItemCompose>,
        unsyncedOrdersCallback: (OrderEntry) -> Unit
    ) {
        viewModelScope.launch {
            mainUseCases.insertOrderUseCase.insertOrderLocal(order, _items).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        unsyncedOrdersCallback(result.data)
                    }
                    is ApiResult.Error -> Unit
                }
            }
        }
    }
}