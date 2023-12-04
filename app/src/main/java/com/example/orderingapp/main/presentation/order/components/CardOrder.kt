package com.example.orderingapp.main.presentation.order.components

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.presentation.components.VoucherDialog

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun CardOrder(order: Order) {
    val activity = LocalContext.current as? Activity
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        VoucherDialog(openDialog, order, activity)
    }
    Card(
        elevation = 8.dp,
        modifier = Modifier.fillMaxWidth(),
        onClick = { openDialog.value = true },
        backgroundColor = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            )
        ) {
            Text(
                text = "Pedido concluído em ${order.date} às ${order.hour}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Ver detalhes do pedido", fontSize = 17.sp)
        }
    }
}
