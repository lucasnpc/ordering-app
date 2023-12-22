package com.example.orderingapp.main.presentation.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orderingapp.R
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.presentation.menu.components.ChangeItemQuantityButtons
import com.example.orderingapp.main.presentation.utils.extensions.brazilianCurrencyFormat
import com.example.orderingapp.main.presentation.utils.extensions.roundDouble
import com.example.orderingapp.main.presentation.utils.extensions.toDateFormat
import com.example.orderingapp.main.presentation.utils.extensions.toHourFormat
import com.example.orderingapp.main.presentation.utils.mappings.composeToItem

@Composable
fun StockScreen(
    items: Map<String, ItemCompose>,
    stockViewModel: StockViewModel = hiltViewModel(),
    purchasesCallback: (PurchaseEntry?) -> Unit
) {
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
        OutlinedButton(
            onClick = {
                val addedItems = items.filter { it.value.quantity.value > 0 }
                stockViewModel.insertPurchase(
                    purchase = Purchase(
                        items = addedItems.composeToItem(),
                        hour = System.currentTimeMillis().toHourFormat(),
                        date = System.currentTimeMillis().toDateFormat(),
                        purchaseValue = addedItems.values.sumOf { it.item.costValue * it.quantity.value }
                            .roundDouble(),
                    ),
                    purchaseCallback = purchasesCallback
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .align(Alignment.BottomCenter),
            enabled = items.any { it.value.quantity.value > 0 }) {
            Text(text = stringResource(R.string.place_purchase))
        }
    }
}