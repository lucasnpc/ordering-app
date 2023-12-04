package com.example.orderingapp.main.presentation.components

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.openPDF
import com.example.orderingapp.main.domain.model.Order

@Composable
fun VoucherDialog(
    openDialog: MutableState<Boolean>,
    order: Order,
    activity: Activity?
) {
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        text = {
            Column {
                VoucherInfo(order = order)
                Divider(modifier = Modifier.height(2.dp))
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        activity?.openPDF(order)
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.see_voucher))
                }
            }
        }
    )
}