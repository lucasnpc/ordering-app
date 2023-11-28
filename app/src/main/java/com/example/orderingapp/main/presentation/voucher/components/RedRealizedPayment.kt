package com.example.orderingapp.main.presentation.voucher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.R

@Composable
fun RedRealizedPayment() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {
        Text(
            text = stringResource(R.string.payment_finished),
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}