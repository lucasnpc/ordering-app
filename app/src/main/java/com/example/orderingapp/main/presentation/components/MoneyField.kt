package com.example.orderingapp.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.orderingapp.commons.extensions.currencyFormat

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoneyField(total: Double, disableButton: (Double) -> Unit) {
    var cashChange by remember { mutableStateOf("") }
    val sum = if (cashChange.isNotEmpty()) cashChange.replace(",", ".").toDouble() - total else 0.0
    val keyboardController = LocalSoftwareKeyboardController.current

    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = cashChange,
            onValueChange = {
                cashChange = it
                if (cashChange.isNotEmpty())
                    disableButton(cashChange.replace(",", ".").toDouble())
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            modifier = Modifier.width(130.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Troco ${if (sum > 0) sum.currencyFormat() else 0.0}",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}