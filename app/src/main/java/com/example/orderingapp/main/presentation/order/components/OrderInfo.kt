package com.example.orderingapp.main.presentation.order.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.Item

@Composable
fun OrderInfo(item: Map.Entry<String, Item>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.value.description,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(150.dp),
            maxLines = 1
        )
        Text(
            text = "Qtd.${item.value.finalQuantity}",
            fontSize = 16.sp
        )
        Text(
            text = (item.value.currentValue * item.value.finalQuantity).currencyFormat(),
            fontSize = 16.sp,
        )
    }
}