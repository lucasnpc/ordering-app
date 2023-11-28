package com.example.orderingapp.main.presentation.voucher.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.Order

@Composable
fun PaymentVoucherInfo(order: Order) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(
                text = stringResource(R.string.payment_voucher),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "${order.date}, ${order.hour}", fontSize = 18.sp)
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "PEDIDO:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        items(order.items) { item ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = item.description, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Qtd. ${item.finalQuantity}", fontSize = 18.sp)
            }
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "VALOR", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = order.items.sumOf { it.finalQuantity * it.currentValue }
                    .currencyFormat(), fontSize = 18.sp)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "PAGAMENTO", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = order.paymentWay, fontSize = 18.sp)
            }
        }
    }
}