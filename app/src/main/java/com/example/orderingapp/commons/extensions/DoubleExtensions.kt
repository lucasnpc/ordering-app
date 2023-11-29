package com.example.orderingapp.commons.extensions

import java.util.Locale

fun Double.roundDouble(): Double {
    return String.format(Locale.US, "%.2f", this).toDouble()
}