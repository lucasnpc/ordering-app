package com.example.orderingapp.main.presentation.voucher.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.Order

@Composable
fun RealizedPaymentInfo(order: Order) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${order.date}, ${order.hour}", fontSize = 20.sp)
        Text(text = order.paymentWay.uppercase(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = order.items.sumOf { it.finalQuantity * it.currentValue }
            .currencyFormat(), fontSize = 20.sp)
    }
}