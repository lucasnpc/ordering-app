package com.example.orderingapp.main.presentation.voucher.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.orderingapp.R
import com.example.orderingapp.commons.pdf.PdfUtil.openPDF
import com.example.orderingapp.main.domain.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VoucherToolbar(drawerState: BottomDrawerState, scope: CoroutineScope, order: Order) {
    val activity = LocalContext.current as? Activity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            scope.launch {
                drawerState.close()
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close Voucher Button",
                tint = Color.White
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            tint = Color.Unspecified,
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
        )
        IconButton(onClick = {
            activity?.run {
                openPDF(order)
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share Voucher",
                tint = Color.White
            )
        }
    }
}