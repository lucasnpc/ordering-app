package com.example.orderingapp.main.presentation.order.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.Order

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun CardOrder(order: Order) {
    Card(elevation = 8.dp, modifier = Modifier.fillMaxWidth(), onClick = {}) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            )
        ) {
            Text(
                text = "Pedido concluído em ${order.date} às ${order.hour}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Items:",
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            order.items.forEach {
                OrderInfo(it)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Total: ${order.orderValue.currencyFormat()}",
                fontSize = 17.sp
            )
        }
    }
}
