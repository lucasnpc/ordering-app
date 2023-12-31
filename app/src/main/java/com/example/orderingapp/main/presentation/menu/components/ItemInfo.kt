package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.main.presentation.utils.extensions.brazilianCurrencyFormat
import com.example.orderingapp.main.domain.model.Item

@Composable
fun ItemInfo(item: Item) {
    Column {
        Text(
            text = item.description,
            fontSize = 18.sp,
            modifier = Modifier.width(180.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = item.currentValue.brazilianCurrencyFormat(), fontSize = 18.sp)
    }
}