package com.example.orderingapp.main.presentation.utils.extensions

import java.text.NumberFormat
import java.util.Locale

fun Double.brazilianCurrencyFormat(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)
}