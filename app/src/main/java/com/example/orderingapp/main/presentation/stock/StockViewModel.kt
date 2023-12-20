package com.example.orderingapp.main.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val mainUseCases: MainUseCases) : ViewModel() {
    fun insertPurchase(purchase: Purchase, purchaseCallback: (PurchaseEntry?) -> Unit) {
        viewModelScope.launch {
            mainUseCases.insertPurchaseUseCase.insertPurchaseLocal(purchase).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        purchaseCallback(result.data)
                    }

                    is ApiResult.Error -> {
                        purchaseCallback(null)
                    }
                }
            }
        }
    }
}