package com.example.orderingapp.main.presentation.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.presentation.menu.components.ChangeItemQuantityButtons
import com.example.orderingapp.main.presentation.utils.extensions.brazilianCurrencyFormat

@Composable
fun StockScreen(items: Map<String, ItemCompose>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items.values.toList()) {
                Card(elevation = 8.dp) {
                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = it.item.description,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = it.item.costValue.brazilianCurrencyFormat(),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        ChangeItemQuantityButtons(it.quantity)
                    }
                }
            }
        }
    }
}