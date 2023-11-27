package com.example.orderingapp.commons.extensions

fun Double.currencyFormat(): String {
    return "R$ ${String.format("%.2f", this).replace(".", ",")}"
}