package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.roundDouble
import com.example.orderingapp.commons.extensions.toDateFormat
import com.example.orderingapp.commons.extensions.toHourFormat
import com.example.orderingapp.commons.mappings.composeToListItem
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.presentation.menu.MenuViewModel

@Composable
fun PaymentOptions(
    addedItems: Map<String, ItemCompose>,
    menuViewModel: MenuViewModel,
    unsyncedOrdersCallback: (List<Order>) -> Unit,
    items: Map<String, ItemCompose>
) {
    val radioOptions = listOf("Crédito", "Débito", "Dinheiro", "Pix")
    val (selectedOption, onOptionSelect) = remember {
        mutableStateOf(radioOptions[0])
    }
    var changeValue by remember {
        mutableStateOf(0.0)
    }
    val total = addedItems.values.sumOf { it.item.currentValue * it.quantity.value }

    Column {
        radioOptions.forEach {
            RadioButtonPaymentOpt(it, selectedOption) { opt ->
                changeValue = 0.0
                onOptionSelect(opt)
            }
        }
        if (selectedOption == stringResource(R.string.money_label))
            MoneyField(total = total) { change ->
                changeValue = change
            }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = {
                menuViewModel.insertOrder(
                    order = Order(
                        items = addedItems.composeToListItem(),
                        hour = System.currentTimeMillis().toHourFormat(),
                        date = System.currentTimeMillis().toDateFormat(),
                        orderValue = total.roundDouble(),
                        paymentWay = selectedOption
                    ),
                    _items = items,
                    unsyncedOrdersCallback = unsyncedOrdersCallback
                )
            },
            enabled = (changeValue >= total || selectedOption != stringResource(R.string.money_label)) && addedItems.isNotEmpty(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.make_payment))
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}
