package com.example.orderingapp.main.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ItemCompose(
    val item: Item,
    var quantity: MutableState<Int> = mutableStateOf(0)
)