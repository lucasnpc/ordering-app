package com.example.orderingapp.main.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orderingapp.main.domain.model.Order

@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel = hiltViewModel(),
    unsyncedOrdersCallback: (List<Order>) -> Unit
) {
    var enableButton by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menuViewModel.items) { item ->
                Text(text = item.description)
                Text(
                    text = item.quantity.value.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = {
                        item.quantity.value++
                    }) {
                        Text(text = "+")
                    }
                    OutlinedButton(onClick = {
                        if (item.quantity.value > 0) {
                            item.quantity.value--
                        }
                    }) {
                        Text(text = "-")
                    }
                }
                enableButton = menuViewModel.items.any { it.quantity.value > 0 }
            }
        }
        OutlinedButton(
            onClick = {
                menuViewModel.insertOrder(unsyncedOrdersCallback)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .align(Alignment.BottomCenter),
            enabled = enableButton
        ) {
            Text(text = "Realizar pedido")
        }
    }
}