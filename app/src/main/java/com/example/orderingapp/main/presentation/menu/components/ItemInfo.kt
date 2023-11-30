package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.ItemCompose

@Composable
fun ItemInfo(itemCompose: ItemCompose) {
    Column {
        Text(
            text = itemCompose.item.description,
            fontSize = 18.sp,
            modifier = Modifier.width(180.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = itemCompose.item.currentValue.currencyFormat(), fontSize = 18.sp)
    }
}