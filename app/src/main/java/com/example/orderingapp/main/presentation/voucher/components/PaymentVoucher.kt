package com.example.orderingapp.main.presentation.voucher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.orderingapp.main.domain.model.Order

@Composable
fun PaymentVoucher(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.White)
    ) {
        RedVoucherToolbar()
        PaymentVoucherInfo(order)
    }
}