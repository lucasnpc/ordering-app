package com.example.orderingapp.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.brazilianCurrencyFormat
import com.example.orderingapp.main.domain.model.Order

@Composable
fun VoucherInfo(order: Order) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(
                text = stringResource(R.string.payment_voucher),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "${order.date}, ${order.hour}",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.order_label),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
        items(order.items.values.toList()) { item ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.description,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black,
                    modifier = Modifier.width(160.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.quantity_label, item.finalQuantity),
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.value_label),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(text = order.items.values.sumOf { it.finalQuantity * it.currentValue }
                    .brazilianCurrencyFormat(), fontSize = 17.sp, color = Color.Black)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.payment_label),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(text = order.paymentWay, fontSize = 17.sp, color = Color.Black)
            }
        }
    }
}