package com.example.orderingapp.commons.extensions

fun Double.roundDouble(): Double{
    return String.format("%.2f", this).toDouble()
}