package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChangeItemQuantityButtons(quantity: MutableState<Int>) {
    Column {
        Row {
            OutlinedButton(
                onClick = {
                    if (quantity.value > 0) {
                        quantity.value--
                    }
                },
                modifier = Modifier.width(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White
                )
            ) {
                Text(text = "-")
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.width(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White
                )
            ) {
                Text(text = quantity.value.toString(), fontSize = 13.sp)
            }
            OutlinedButton(
                onClick = {
                    quantity.value++
                },
                modifier = Modifier.width(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White
                )

            ) {
                Text(text = "+")
            }
        }
    }
}