package com.example.orderingapp.main.presentation.utils.extensions

import java.util.Locale

fun Double.roundDouble(): Double {
    return String.format(Locale.US, "%.2f", this).toDouble()
}