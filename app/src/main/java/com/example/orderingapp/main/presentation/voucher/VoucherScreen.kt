package com.example.orderingapp.main.presentation.voucher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.orderingapp.commons.extensions.convertToOrder
import com.example.orderingapp.main.presentation.voucher.components.PaymentVoucher
import com.example.orderingapp.main.presentation.voucher.components.RealizedPaymentInfo
import com.example.orderingapp.main.presentation.voucher.components.RedRealizedPayment
import com.example.orderingapp.main.presentation.voucher.components.SeeVoucherBt
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VoucherScreen(orderJson: String) {
    orderJson.convertToOrder()?.let { order ->
        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        val scope = rememberCoroutineScope()
        BottomDrawer(
            drawerState = drawerState,
            drawerContent = {
                PaymentVoucher(
                    order
                )
            }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        RedRealizedPayment()
                        Spacer(modifier = Modifier.height(32.dp))
                        RealizedPaymentInfo(order)
                        Spacer(modifier = Modifier.height(32.dp))
                        Divider(modifier = Modifier.height(2.dp))
                        SeeVoucherBt {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    }
                }
            }
        }
    }

}