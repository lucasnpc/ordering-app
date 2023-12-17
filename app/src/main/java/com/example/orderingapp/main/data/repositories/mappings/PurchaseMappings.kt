package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.PurchaseDTO
import com.example.orderingapp.main.domain.model.Purchase

fun Purchase.toPurchaseDTO(): PurchaseDTO {
    return PurchaseDTO(
        items = items.mapValues { it.value.finalQuantity },
        date = date,
        hour = hour,
        purchaseValue = purchaseValue,
        paymentWay = paymentWay,
    )
}