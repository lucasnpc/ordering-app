package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RadioButtonPaymentOpt(
    it: String,
    selectedOption: String,
    onOptionSelect: (String) -> Unit
) {
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