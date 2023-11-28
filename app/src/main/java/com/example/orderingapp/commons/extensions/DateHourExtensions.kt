package com.example.orderingapp.commons.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toDateFormat(): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(this).toString()
}

fun Long.toHourFormat(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(this).toString()
}