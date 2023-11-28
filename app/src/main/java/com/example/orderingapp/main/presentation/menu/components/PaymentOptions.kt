package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.toDateFormat
import com.example.orderingapp.commons.extensions.toHourFormat
import com.example.orderingapp.commons.mappings.composeToListItem
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.presentation.components.MoneyField
import com.example.orderingapp.main.presentation.menu.MenuViewModel

@Composable
fun PaymentOptions(
    addedItems: List<ItemCompose>,
    menuViewModel: MenuViewModel,
    unsyncedOrdersCallback: (List<Order>) -> Unit
) {
    val radioOptions = listOf("Crédito", "Débito", "Dinheiro", "Pix")
    val (selectedOption, onOptionSelect) = remember {
        mutableStateOf(radioOptions[0])
    }
    var changeValue by remember {
        mutableStateOf(0.0)
    }
    val total = addedItems.sumOf { it.item.currentValue * it.quantity.value }

    Column {
        radioOptions.forEach {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (it == selectedOption),
                        onClick = {
                            onOptionSelect(it)
                        },
                        role = Role.RadioButton
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                RadioButton(
                    selected = it == selectedOption,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = MaterialTheme.colors.onBackground,
                        selectedColor = MaterialTheme.colors.primary
                    )
                )
                Text(
                    text = it,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(70.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
        if (selectedOption == "Dinheiro")
            MoneyField(total = total) { change ->
                changeValue = change
            }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = {
                menuViewModel.insertOrder(
                    Order(
                        items = addedItems.composeToListItem(),
                        hour = System.currentTimeMillis().toHourFormat(),
                        date = System.currentTimeMillis().toDateFormat(),
                        orderValue = addedItems.sumOf { it.item.currentValue * it.quantity.value },
                        paymentWay = selectedOption
                    ), unsyncedOrdersCallback
                )
            },
            enabled = changeValue >= total || selectedOption != "Dinheiro",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.effect_payment))
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}