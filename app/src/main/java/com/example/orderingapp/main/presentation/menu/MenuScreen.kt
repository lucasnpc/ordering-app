package com.example.orderingapp.main.presentation.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orderingapp.R
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.presentation.menu.components.ItemsList
import com.example.orderingapp.main.presentation.menu.components.PaymentOptions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuScreen(
    items: Map<String, ItemCompose>,
    menuViewModel: MenuViewModel = hiltViewModel(),
    ordersCallback: (OrderEntry?) -> Unit
) {
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()
    BottomDrawer(
        gesturesEnabled = !drawerState.isClosed,
        drawerState = drawerState,
        drawerContent = {
            PaymentOptions(
                addedItems = items.filter { it.value.quantity.value > 0 },
            ) { addedOrder ->
                scope.launch {
                    drawerState.close()
                }
                menuViewModel.insertOrder(
                    order = addedOrder,
                    ordersCallback = ordersCallback
                )
            }
        }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ItemsList(list = items.values.toList())
            OutlinedButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .align(Alignment.BottomCenter),
                enabled = items.any { it.value.quantity.value > 0 }
            ) {
                Text(text = stringResource(R.string.place_order))
            }
        }
    }
}