package com.example.orderingapp.main.presentation.voucher.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.Job

@Composable
fun PaymentVoucher(order: Order, closeClick: () -> Job) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RedVoucherToolbar { closeClick() }
        PaymentVoucherInfo(order)
    }
}