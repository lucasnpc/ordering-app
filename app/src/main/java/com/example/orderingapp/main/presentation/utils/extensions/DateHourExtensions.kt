package com.example.orderingapp.main.presentation.utils.extensions

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toDateFormat(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(this).toString()
}

fun Long.toHourFormat(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(this).toString()
}

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
}